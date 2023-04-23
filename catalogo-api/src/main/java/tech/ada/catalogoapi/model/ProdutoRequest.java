package tech.ada.catalogoapi.model;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotNull(message = "O campo 'nome' não pode ser nulo.")
        @NotBlank(message = "O campo 'nome' não pode ser vazio.")
        String nome,
        @NotNull(message = "O campo 'preco' não pode ser nulo.")
        @DecimalMin(value = "0.0", message = "O campo 'preco' não pode ser negativo.")
        @Digits(integer = 10,fraction = 2, message = "O campo 'preco' deve ser um valor monetário.")
        BigDecimal preco,
        @NotNull(message = "O campo 'quantidade' não pode ser nulo.")
        @PositiveOrZero(message = "O campo 'quantidade' deve ser um número positivo")
        Integer quantidade) {
}
