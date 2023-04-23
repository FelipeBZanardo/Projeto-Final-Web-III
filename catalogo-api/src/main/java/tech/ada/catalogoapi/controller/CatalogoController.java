package tech.ada.catalogoapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.catalogoapi.model.Produto;
import tech.ada.catalogoapi.model.ProdutoRequest;
import tech.ada.catalogoapi.service.CatalogoService;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @PostMapping
    public Mono<Produto> save(@Valid @RequestBody ProdutoRequest produtoRequest){
        return Mono.defer(() -> catalogoService.save(produtoRequest))
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping
    public Flux<Produto> findAll(){
        return Flux.defer(catalogoService::findAll)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/{id}")
    public Mono<Produto> findById(@PathVariable String id){
        return Mono.defer(() -> catalogoService.findById(id))
                .subscribeOn(Schedulers.parallel());
    }

    @PatchMapping ("/{id}")
    public Mono<Produto> updateQuantidade(@PathVariable String id, @RequestBody Integer quantidade){
        return Mono.defer(() -> catalogoService.updateQuantidade(id, quantidade))
                .subscribeOn(Schedulers.parallel());
    }
}
