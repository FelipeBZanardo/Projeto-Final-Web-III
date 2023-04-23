package tech.ada.clienteapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(@NotNull(message = "O campo 'nome' não pode ser nulo.")
                             @NotBlank(message = "O campo 'nome' não pode ser vazio.")
                             String nome,
                             @NotNull(message = "O campo 'email' não pode ser nulo.")
                             @Email(message = "Digite um e-mail válido.")
                             String email) {
}
