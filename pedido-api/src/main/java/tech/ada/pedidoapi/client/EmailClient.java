package tech.ada.pedidoapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.ada.pedidoapi.client.dto.Email;

@Component
@Slf4j
public class EmailClient {

    private final WebClient client;
    private final ObjectMapper mapper;

    public EmailClient(WebClient.Builder clientBuilder, ObjectMapper mapper) {
        this.client = clientBuilder
                .baseUrl("http://localhost:8080")
                .build();
        this.mapper = mapper;
    }

    public Mono<Email> enviarEmail(Email email){
        String payload = "";
        try{
            payload = mapper.writeValueAsString(email);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return this.client
                .post()
                .uri("/email")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful()){
                        log.info("E-mail enviado com sucesso - {}", email);
                        return result.bodyToMono(Email.class);
                    }
                    return Mono.error(new RuntimeException("Erro na chamada da api de e-mail"));
                });
    }
}
