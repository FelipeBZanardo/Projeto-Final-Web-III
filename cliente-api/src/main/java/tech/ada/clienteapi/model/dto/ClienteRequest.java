package tech.ada.clienteapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
                             @NotBlank(message = "O campo 'nome' não pode ser vazio ou nulo.")
                             String nome,
                             @NotBlank(message = "O campo 'email' não pode ser nulo.")
                             @Email(message = "Digite um e-mail válido.")
                             String email) {
}
