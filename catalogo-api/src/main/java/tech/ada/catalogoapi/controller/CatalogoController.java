package tech.ada.catalogoapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.catalogoapi.model.dto.ProdutoRequest;
import tech.ada.catalogoapi.model.dto.ProdutoResponse;
import tech.ada.catalogoapi.service.CatalogoService;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProdutoResponse> save(@Valid @RequestBody ProdutoRequest produtoRequest){
        return Mono.defer(() -> catalogoService.save(produtoRequest))
                .map(ProdutoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping
    public Flux<ProdutoResponse> findAll(){
        return Flux.defer(catalogoService::findAll)
                .map(ProdutoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/{id}")
    public Mono<ProdutoResponse> findById(@PathVariable String id){
        return Mono.defer(() -> catalogoService.findById(id))
                .map(ProdutoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @PatchMapping ("/{id}")
    public Mono<ProdutoResponse> updateQuantidade(@PathVariable String id, @RequestBody Integer quantidade){
        return Mono.defer(() -> catalogoService.updateQuantidade(id, quantidade))
                .map(ProdutoResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }
}
