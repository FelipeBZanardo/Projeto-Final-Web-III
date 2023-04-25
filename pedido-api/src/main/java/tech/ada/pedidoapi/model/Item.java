package tech.ada.pedidoapi.model;

import java.math.BigDecimal;

public record Item(
        String idProduto,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario) { }
