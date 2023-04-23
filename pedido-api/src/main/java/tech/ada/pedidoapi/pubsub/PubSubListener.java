package tech.ada.pedidoapi.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import tech.ada.pedidoapi.client.CatalogoClient;
import tech.ada.pedidoapi.client.EmailClient;
import tech.ada.pedidoapi.client.dto.Email;
import tech.ada.pedidoapi.model.Pedido;
import tech.ada.pedidoapi.model.Status;
import tech.ada.pedidoapi.repository.PedidoRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;


@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubListener implements InitializingBean {

    private final Sinks.Many<PubSubMessage> sink;
    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final EmailClient emailClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        sink.asFlux()
                .delayElements(Duration.ofSeconds(5))
                .subscribe(
                        next -> {
                            log.info("Iniciando listener onNext -{}", next);
                            pedidoRepository.findById(next.pedido().getId())
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .flatMap(pedido -> {
                                        log.info("Verificando Status do Pedido - {}", pedido);
                                        if(pedido.getStatus().equals(Status.REALIZADO)){
                                            log.info("Pedido confirmado. Está pronto para ser enviado - {}",pedido );
                                            return pedidoRepository.save(
                                                    pedido.withStatus(Status.CONFIRMADO).withData(Instant.now()));
                                        }
                                        log.error("Pedido não confirmado. Erro ao efetuar compra");
                                        return Mono.defer(() -> pedidoRepository.save(pedido));
                                    })
                                    .flatMap(pedido -> {
                                        log.info("Verificando Status do Pedido - {}", pedido);
                                        if(pedido.getStatus().equals(Status.CONFIRMADO)){
                                            log.info("Enviando pedido - {}", pedido);
                                            return pedidoRepository.save(
                                                    pedido.withStatus(Status.ENVIADO).withData(Instant.now()));
                                        }
                                        log.error("Pedido não enviado. Erro ao efetuar compra");
                                        return Mono.defer(() -> pedidoRepository.save(pedido));
                                    })
                                    .flatMap(pedido -> {
                                        if(pedido.getStatus().equals(Status.ENVIADO)){
                                            log.info("Atualizando Estoque na Api de Catálogo - {}", pedido);
                                            pedido.getItens().forEach(item ->
                                                    catalogoClient.atualizarQuantidade(item.idProduto(), item.quantidade())
                                                            .subscribe());
                                        }
                                        return Mono.defer(() -> pedidoRepository.findById(pedido.getId()));
                                    })
                                    .flatMap(pedido -> {
                                        if(pedido.getStatus().equals(Status.ENVIADO)){
                                            log.info("Enviando e-mail com detalhes da compra - {}", pedido);
                                            return enviarEmail(pedido).
                                                    flatMap(__ -> pedidoRepository.findById(pedido.getId()));
                                        }
                                        return Mono.defer(() -> pedidoRepository.findById(pedido.getId()));
                                    })
                                    .doOnError(throwable -> {
                                        log.error("Algo deu errado com seu pedido - {}", throwable.getMessage());
                                        pedidoRepository.save(next.pedido().withStatus(Status.ERRO_NO_PEDIDO)).subscribe();
                                    })
                                    .subscribe();
                        },
                        err -> log.error("Error: {}", err.getMessage()),
                        () -> log.info("Completed")
                );
    }

    private Mono<Email> enviarEmail(Pedido pedido) {
        String destinatario = pedido.getCliente().email();
        String assunto = "Seu pedido foi ENVIADO!";
        StringBuilder mensagem = new StringBuilder("""
                Parabéns seu pedido foi ENVIADO e logo chegará até você.
                
                Detalhes da compra:
                
                %-30s  %-20s  %-20s  %-20s
                """.formatted("Produto", "Quantidade", "Preço unitário(R$)", "Total(R$)"));

        return Flux.fromIterable(pedido.getItens()).
                subscribeOn(Schedulers.boundedElastic())
                .flatMap(itemRequest ->
                                catalogoClient.getProdutoById(itemRequest.idProduto())
                                        .map(produto -> new StringBuilder("%-30s  %-20s  %-20s  %-20s%n".formatted(
                                                produto.nome(), itemRequest.quantidade(),
                                                produto.preco(),
                                                produto.preco().multiply(BigDecimal.valueOf(itemRequest.quantidade())))
                                        )))
                .reduce(StringBuilder::append)
                .flatMap(msg -> {
                    mensagem.append(msg.toString()).append("\n\nTotal do Pedido: R$").append(pedido.getTotal());
                    return emailClient.enviarEmail(new Email(destinatario, assunto, mensagem.toString()));
                });

    }
}
