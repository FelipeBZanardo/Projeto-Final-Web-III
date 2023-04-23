package tech.ada.pedidoapi.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.model.dto.ItemRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@With
@Document("pedidos")
public class Pedido {
    private String id;
    private Cliente cliente;
    private List<ItemRequest> itens;
    private Instant data;
    private Status status;
    private BigDecimal total;
}
