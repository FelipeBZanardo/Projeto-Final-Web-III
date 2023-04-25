package tech.ada.pedidoapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.pedidoapi.model.dto.PedidoRequest;
import tech.ada.pedidoapi.model.dto.PedidoResponse;
import tech.ada.pedidoapi.pubsub.PullPedidosComponent;
import tech.ada.pedidoapi.service.PedidoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    private final PullPedidosComponent pullPagamentosComponent;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PedidoResponse> efetuarPedido(@Valid @RequestBody PedidoRequest pedidoRequest){
        return Mono.defer(() -> pedidoService.efetuarPedido(pedidoRequest))
                .flatMap(pullPagamentosComponent::pullNewPedido)
                .map(PedidoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping
    public Flux<PedidoResponse> getAllPedidos(){
        return Flux.defer(pedidoService::getAll)
                .map(PedidoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/{id}")
    public Mono<PedidoResponse> getPedidoById(@PathVariable String id){
        return Mono.defer(() -> pedidoService.getPedidoById(id))
                .map(PedidoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }
}
