package tech.ada.pedidoapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.pedidoapi.model.Pedido;
import tech.ada.pedidoapi.model.dto.PedidoRequest;
import tech.ada.pedidoapi.pubsub.PullPedidosComponent;
import tech.ada.pedidoapi.service.PedidoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    private final PullPedidosComponent pullPagamentosComponent;

    @PostMapping
    public Mono<Pedido> efetuarPedido(@Valid @RequestBody PedidoRequest pedidoRequest){
        return Mono.defer(() -> pedidoService.efetuarPedido(pedidoRequest))
                .flatMap(pullPagamentosComponent::pullNewPedido)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping
    public Flux<Pedido> getAllPedidos(){
        return Flux.defer(pedidoService::getAll)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/{id}")
    public Mono<Pedido> getPedidoById(@PathVariable String id){
        return Mono.defer(() -> pedidoService.getPedidoById(id))
                .subscribeOn(Schedulers.parallel());
    }
}
