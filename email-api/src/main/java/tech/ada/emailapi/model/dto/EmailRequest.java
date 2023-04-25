package tech.ada.emailapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailRequest(
        @NotNull(message = "O campo 'destinatario' não pode ser nulo.")
        @Email(message = "Digite um e-mail válido para o destinatário.")
        String destinatario,
        @NotBlank(message = "O campo 'assunto' não pode ser vazio ou nulo.")
        String assunto,
        @NotBlank(message = "O campo 'mensagem' não pode ser vazio ou nulo.")
        String mensagem) {
}
