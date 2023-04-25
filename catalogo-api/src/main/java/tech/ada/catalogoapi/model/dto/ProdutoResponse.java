package tech.ada.catalogoapi.model.dto;

import lombok.Builder;
import lombok.Data;
import tech.ada.catalogoapi.model.Produto;

import java.math.BigDecimal;

@Data
@Builder
public class ProdutoResponse {
    private String id;
    private String nome;
    private BigDecimal preco;
    private Integer quantidade;

    public static ProdutoResponse convertToResponse(Produto produto){
        return ProdutoResponse.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .preco(produto.getPreco())
                .quantidade(produto.getQuantidade())
                .build();
    }
}
