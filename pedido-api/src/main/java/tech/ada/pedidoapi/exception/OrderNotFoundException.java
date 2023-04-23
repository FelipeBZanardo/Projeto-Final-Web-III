package tech.ada.pedidoapi.exception;

public class OrderNotFoundException extends RuntimeException{

    private final String id;

    public OrderNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Id de Pedido não encontrado. Digite um Id válido - " + id;
    }
}
