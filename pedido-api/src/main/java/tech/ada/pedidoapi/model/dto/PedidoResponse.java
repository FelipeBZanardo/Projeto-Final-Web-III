package tech.ada.pedidoapi.model.dto;

import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.model.Status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoResponse(String id,
        Cliente cliente,
        List<ItemResponse> itens,
        Instant data,
        Status status,
        BigDecimal total) {
}
