package tech.ada.pedidoapi.model.dto;

import java.math.BigDecimal;

public record ItemResponse(String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
}
