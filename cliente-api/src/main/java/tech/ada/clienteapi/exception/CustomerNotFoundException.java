package tech.ada.clienteapi.exception;

public class CustomerNotFoundException extends RuntimeException{

    private final String id;

    public CustomerNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Id de Cliente não encontrado. Digite um Id válido - " + id;
    }
}
