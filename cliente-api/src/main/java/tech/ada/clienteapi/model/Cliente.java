package tech.ada.clienteapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("clientes")
public class Cliente {
    private String id;
    private String nome;
    private String email;
}
