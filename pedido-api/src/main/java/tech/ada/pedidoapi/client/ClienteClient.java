package tech.ada.pedidoapi.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.exception.CustomerNotFoundException;

@Component
@Slf4j
public class ClienteClient {

    private final WebClient client;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    public ClienteClient(WebClient.Builder clientBuilder, ReactiveCircuitBreakerFactory<?, ?> reactiveCircuitBreakerFactory) {
        this.client = clientBuilder
                .baseUrl("http://localhost:8080")
                .build();
        this.reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("cliente-api-circuit-breaker");
    }


    public Mono<Cliente> getclienteByIdCircuitBreaker(String id){
        return reactiveCircuitBreaker.run(executarGetClienteById(id), this::fallbackMethod);
    }

    private <T> Mono<T> fallbackMethod(Throwable throwable) {
        log.error("Entrando no método de fallback", throwable);
        return Mono.empty();
    }

    private Mono<Cliente> executarGetClienteById(String id){
        return this.client
                .get()
                .uri("/clientes/" + id)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful())
                        return result.bodyToMono(Cliente.class);
                    else
                        return Mono.error(() -> {
                            log.error("Id de Cliente não encontrado. Digite um Id válido - {}", id);
                            return new CustomerNotFoundException(id);
                        });

                });
    }

    public Mono<Cliente> getClienteById(String id){
        return executarGetClienteById(id);
    }

}
