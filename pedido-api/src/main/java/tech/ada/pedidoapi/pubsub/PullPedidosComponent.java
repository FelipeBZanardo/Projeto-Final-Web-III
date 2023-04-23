package tech.ada.pedidoapi.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import tech.ada.pedidoapi.model.Pedido;


@Slf4j
@Component
@RequiredArgsConstructor
public class PullPedidosComponent {

    private final Sinks.Many<PubSubMessage> sink;

    public Mono<Pedido> pullNewPedido(final Pedido pedido){
        return Mono.fromCallable(() -> {
            log.info("Iniciando pull de pedido - {}", pedido);
            return new PubSubMessage(pedido);
        })
                .subscribeOn(Schedulers.parallel())
                .doOnNext(this.sink::tryEmitNext)
                .doOnNext(evento -> log.info("Evento de pedido criado - {}", evento))
                .thenReturn(pedido);
    }
}
