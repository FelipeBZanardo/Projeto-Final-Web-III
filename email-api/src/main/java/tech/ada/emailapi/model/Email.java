package tech.ada.emailapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("emails")
@AllArgsConstructor
public class Email {
    private String id;
    private String remetente;
    private String destinatario;
    private String assunto;
    private String mensagem;
}
