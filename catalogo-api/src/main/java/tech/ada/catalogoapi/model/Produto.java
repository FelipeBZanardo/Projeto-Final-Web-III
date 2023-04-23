package tech.ada.catalogoapi.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@Document(value = "produtos")
public class Produto {
    private String id;
    private String nome;
    private BigDecimal preco;
    private Integer quantidade;
}
