package tech.ada.pedidoapi.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.exception.CustomerNotFoundException;

@Component
@Slf4j
public class ClienteClient {

    private final WebClient client;

    public ClienteClient(WebClient.Builder clientBuilder) {
        this.client = clientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }


    public Mono<Cliente> getClienteById(String id){
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

}
