package tech.ada.pedidoapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.ada.pedidoapi.client.dto.Produto;
import tech.ada.pedidoapi.exception.ProductNotFoundException;

@Component
@Slf4j
public class CatalogoClient {

    private final WebClient client;
    private final ObjectMapper mapper;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    public CatalogoClient(WebClient.Builder clientBuilder, ObjectMapper mapper,
                          ReactiveCircuitBreakerFactory<?, ?> reactiveCircuitBreakerFactory) {
        this.client = clientBuilder
                .baseUrl("http://localhost:8080")
                .build();
        this.mapper = mapper;
        this.reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("catalogo-api-circuit-breaker");
    }

    private Mono<Produto> executarBuscaPorId(String id){
        return this.client
                .get()
                .uri("/catalogo/" + id)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful())
                        return result.bodyToMono(Produto.class);
                    else
                        return Mono.error(() -> {
                            log.error("Id de Produto não encontrado. Digite um Id válido - {}", id);
                            return new ProductNotFoundException(id);
                        });

                });
    }

    public Mono<Produto> getProdutoById(String id){
        return executarBuscaPorId(id);
    }

    public Mono<Produto> getProdutoByIdCircuitBreaker(String id){
        return this.reactiveCircuitBreaker
                .run(executarBuscaPorId(id), this::fallbackMethod);
    }

    private <T> Mono<T> fallbackMethod(Throwable throwable) {
        log.error("Entrando no método de fallback", throwable);
        return Mono.empty();
    }

    private Mono<Produto> exexutarAtualizarQuantidade(String id, Integer quantidade){
        String payload = "";
        try{
            payload = mapper.writeValueAsString(quantidade);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return this.client
                .patch()
                .uri("/catalogo/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful()){
                        log.info("Estoque do produto atualizado com sucesso - {}", id);
                        return result.bodyToMono(Produto.class);
                    }
                    return Mono.error(() -> {
                        log.error("Id de Produto não encontrado. Digite um Id válido - {}", id);
                        return new ProductNotFoundException(id);
                    });
                });
    }

    public Mono<Produto> atualizarQuantidade(String id, Integer quantidade){
        return exexutarAtualizarQuantidade(id, quantidade);
    }

    public Mono<Produto> atualizarQuantidadeCircuitBreaker(String id, Integer quantidade){
        return this.reactiveCircuitBreaker
                .run(exexutarAtualizarQuantidade(id, quantidade), this::fallbackMethod);
    }
}
