package tech.ada.emailapi.model.dto;

import lombok.Builder;
import lombok.Data;
import tech.ada.emailapi.model.Email;

@Data
@Builder
public class EmailResponse {
    private String id;
    private String remetente;
    private String destinatario;
    private String assunto;
    private String mensagem;

    public static EmailResponse convertToResponse(Email email){
        return EmailResponse.builder()
                .id(email.getId())
                .remetente(email.getRemetente())
                .destinatario(email.getDestinatario())
                .assunto(email.getAssunto())
                .mensagem(email.getMensagem())
                .build();
    }
}
