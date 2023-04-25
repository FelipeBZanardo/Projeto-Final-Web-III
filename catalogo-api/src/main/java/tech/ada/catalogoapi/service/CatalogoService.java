package tech.ada.catalogoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.catalogoapi.exception.ProductNotFoundException;
import tech.ada.catalogoapi.model.Produto;
import tech.ada.catalogoapi.model.dto.ProdutoRequest;
import tech.ada.catalogoapi.repository.CatalogoRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;
    public Flux<Produto> findAll() {
        log.info("Iniciando busca de todos os produtos do catálago no banco de dados");
        return catalogoRepository.findAll()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(produto -> log.info("Resgatando Produtos do catálago do banco de dados - {}", produto));
    }

    public Mono<Produto> findById(String id) {
        log.info("Iniciando busca do Produto do catálago no banco de dados - {}", id);
        return catalogoRepository.findById(id)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(produto -> log.info("Resgatando Produto do catálago no banco de dados - {}", produto))
                .switchIfEmpty(Mono.error(() -> {
                    log.error("Id de Produto não encontrado. Digite um Id válido - {}", id);
                    return new ProductNotFoundException(id);
                }));
    }

    public Mono<Produto> save(ProdutoRequest produtoRequest) {
        log.info("Iniciando persistência do Produto do catálago no banco de dados - {}", produtoRequest);
        Produto produto = Produto.builder()
                .id(UUID.randomUUID().toString())
                .nome(produtoRequest.nome())
                .preco(produtoRequest.preco())
                .quantidade(produtoRequest.quantidade())
                .build();
        return catalogoRepository.save(produto)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(prod -> log.info("Produto persistido no banco de dados - {}", prod));
    }

    public Mono<Produto> updateQuantidade(String id, Integer quantidade) {
        log.info("Iniciando atualização do estoque - {}", id);
        return Mono.defer(() -> findById(id))
                .flatMap(produto -> {
                    produto.setQuantidade(produto.getQuantidade() - quantidade);
                    log.info("Estoque atualizado com sucesso - {}", produto);
                    return catalogoRepository.save(produto);
                });
    }
}
