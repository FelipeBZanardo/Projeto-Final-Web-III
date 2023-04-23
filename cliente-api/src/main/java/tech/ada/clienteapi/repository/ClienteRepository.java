package tech.ada.clienteapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import tech.ada.clienteapi.model.Cliente;

public interface ClienteRepository extends ReactiveCrudRepository<Cliente, String> {
}
