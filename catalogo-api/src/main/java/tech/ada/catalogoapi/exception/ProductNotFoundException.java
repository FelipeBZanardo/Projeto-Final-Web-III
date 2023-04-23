package tech.ada.catalogoapi.exception;

public class ProductNotFoundException extends RuntimeException{

    private final String id;

    public ProductNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Id de Produto não encontrado. Digite um Id válido - " + id;
    }
}
