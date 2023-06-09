package tech.ada.pedidoapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequest(
        @NotBlank(message = "O campo 'idProduto' não pode ser vazio ou nulo.")
        String idProduto,
        @NotNull(message = "O campo 'quantidade' não pode ser nulo.")
        @Positive(message = "O campo 'quantidade' deve ser um número positivo")
        Integer quantidade) { }
