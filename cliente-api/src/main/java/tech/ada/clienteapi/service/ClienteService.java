package tech.ada.clienteapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.clienteapi.exception.CustomerNotFoundException;
import tech.ada.clienteapi.model.Cliente;
import tech.ada.clienteapi.model.dto.ClienteRequest;
import tech.ada.clienteapi.repository.ClienteRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Mono<Cliente> save(ClienteRequest clienteRequest) {
        log.info("Iniciando persistência no banco de dados de Clientes");
        return Mono.defer(() -> {
            log.info("Cliente salvo no banco de dados - {}", clienteRequest);
            return clienteRepository.save(new Cliente(UUID.randomUUID().toString(),
                    clienteRequest.nome(), clienteRequest.email()));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Cliente> getClienteById(String id) {
        log.info("Iniciando busca no banco de dados - {}", id);
        return Mono.defer(() -> clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> {
                    log.error("Id de Cliente não encontrado. Digite um Id válido - {}", id);
                    return new CustomerNotFoundException(id);
                })))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
