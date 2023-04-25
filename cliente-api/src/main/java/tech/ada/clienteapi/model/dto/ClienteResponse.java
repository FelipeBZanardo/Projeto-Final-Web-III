package tech.ada.clienteapi.model.dto;

import lombok.Builder;
import lombok.Data;
import tech.ada.clienteapi.model.Cliente;

@Builder
@Data
public class ClienteResponse {
    private String id;
    private String nome;
    private String email;

    public static ClienteResponse convertToResponse(Cliente cliente){
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .build();
    }
}
