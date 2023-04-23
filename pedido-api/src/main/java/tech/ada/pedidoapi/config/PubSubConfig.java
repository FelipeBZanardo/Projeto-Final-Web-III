package tech.ada.pedidoapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;
import tech.ada.pedidoapi.pubsub.PubSubMessage;

@Configuration
public class PubSubConfig {

    @Bean
    public Sinks.Many<PubSubMessage> sink(){
        return Sinks.many()
                .multicast()
                .onBackpressureBuffer(1024);
    }
}
