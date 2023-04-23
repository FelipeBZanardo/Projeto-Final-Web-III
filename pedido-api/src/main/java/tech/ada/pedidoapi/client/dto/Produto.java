package tech.ada.pedidoapi.client.dto;

import java.math.BigDecimal;

public record Produto(String id,String nome, BigDecimal preco, Integer quantidade) {
}
