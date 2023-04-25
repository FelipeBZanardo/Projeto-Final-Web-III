package tech.ada.pedidoapi.exception;

import tech.ada.pedidoapi.client.dto.Produto;

public class InvalidAmountException extends RuntimeException{

    private final Produto produto;

    public InvalidAmountException(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String getMessage() {
        return "Quantidade do produto '%s - %s' insuficiente. Há apenas %s no estoque."
                .formatted(produto.nome(), produto.id(), produto.quantidade());
    }
}
