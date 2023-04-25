package tech.ada.pedidoapi.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(
        @NotBlank(message = "O campo 'idCliente' não pode ser vazio.")
        String idCliente,
        @NotNull(message = "O campo 'itens' não pode ser nulo")
        List<@Valid ItemRequest> itens) {
}
