package tech.ada.emailapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("emails")
@AllArgsConstructor
public class Email {
    private String id;
    @NotNull
    private String remetente;
    private String destinatario;
    private String assunto;
    private String mensagem;
}
