package tech.ada.pedidoapi.exception;

import tech.ada.pedidoapi.client.dto.Produto;

public class InvalidAmountException extends RuntimeException{

    private final Produto produto;

    public InvalidAmountException(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String getMessage() {
        return "Quantidade do produto '" + produto.nome() + "' insuficiente. Há apenas " + produto.quantidade() + " no estoque";
    }
}
