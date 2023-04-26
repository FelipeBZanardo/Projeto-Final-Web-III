package tech.ada.pedidoapi.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
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
                                        if(verificarStatusPedido(pedido, Status.REALIZADO,
                                                "Pedido confirmado. Está pronto para ser enviado"))
                                            return pedidoRepository.save(
                                                    pedido.withStatus(Status.CONFIRMADO).withData(Instant.now()));
                                        return Mono.defer(() -> pedidoRepository.save(pedido));
                                    })
                                    .flatMap(pedido -> {
                                        if(verificarStatusPedido(pedido, Status.CONFIRMADO,
                                                "Enviando pedido"))
                                            return pedidoRepository.save(
                                                    pedido.withStatus(Status.ENVIADO).withData(Instant.now()));
                                        return Mono.defer(() -> pedidoRepository.save(pedido));
                                    })
                                    .flatMap(pedido -> {
                                        if(verificarStatusPedido(pedido, Status.ENVIADO,
                                                "Atualizando Estoque na Api de Catálogo"))
                                            pedido.getItens().forEach(item ->
                                                    //catalogoClient.atualizarQuantidade(item.idProduto(), item.quantidade())
                                                    catalogoClient.atualizarQuantidadeCircuitBreaker(item.idProduto(), item.quantidade())
                                                            .subscribe());
                                        return Mono.defer(() -> pedidoRepository.findById(pedido.getId()));
                                    })
                                    .flatMap(pedido -> {
                                        if(verificarStatusPedido(pedido, Status.ENVIADO,
                                                "Enviando e-mail com detalhes da compra"))
                                            return enviarEmail(pedido).
                                                    flatMap(__ -> pedidoRepository.findById(pedido.getId()));
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

    private boolean verificarStatusPedido(Pedido pedido, Status status, String mensagemLog){
        log.info("Verificando Status do Pedido - {}", pedido);
        if(pedido.getStatus().equals(status)) {
            log.info("{} - {}", mensagemLog, pedido);
            return true;
        }
        log.error("Erro!");
        return false;
    }

    private Mono<Email> enviarEmail(Pedido pedido) {
        String destinatario = pedido.getCliente().email();
        String assunto = "Seu pedido foi ENVIADO!";
        StringBuilder mensagem = new StringBuilder("""
                Parabéns %s, seu pedido foi ENVIADO e logo chegará até você.
                                
                Detalhes da compra:
                                
                %-30s  %-20s  %-20s  %-20s
                """.formatted(pedido.getCliente().nome(), "Produto", "Quantidade", "Preço unitário(R$)", "Total(R$)"));

        StringBuilder msg = pedido.getItens().stream()
                .map(item -> new StringBuilder("%-30s  %-20s  %-20s  %-20s%n".formatted(
                        item.nomeProduto(), item.quantidade(),
                        item.valorUnitario(),
                        item.valorUnitario().multiply(BigDecimal.valueOf(item.quantidade())))))
                .reduce(StringBuilder::append)
                .get();

        mensagem.append(msg.append("\n\nTotal do Pedido: R$").append(pedido.getTotal()));
        return emailClient.enviarEmail(new Email(destinatario, assunto, mensagem.toString()));
        //return emailClient.enviarEmailCircuitBreaker(new Email(destinatario, assunto, mensagem.toString()));
    }
}