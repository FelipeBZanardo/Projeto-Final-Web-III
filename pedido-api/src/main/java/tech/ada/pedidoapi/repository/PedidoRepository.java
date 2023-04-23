package tech.ada.pedidoapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import tech.ada.pedidoapi.model.Pedido;

public interface PedidoRepository extends ReactiveCrudRepository<Pedido, String> {
}
