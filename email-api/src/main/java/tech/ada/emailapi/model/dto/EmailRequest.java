package tech.ada.emailapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailRequest(
        @NotNull(message = "O campo 'destinatario' não pode ser nulo.")
        @Email(message = "Digite um e-mail válido para o destinatário.")
        String destinatario,
        @NotNull(message = "O campo 'assunto' não pode ser nulo.")
        String assunto,
        @NotNull(message = "O campo 'mensagem' não pode ser nulo.")
        String mensagem) {
}
