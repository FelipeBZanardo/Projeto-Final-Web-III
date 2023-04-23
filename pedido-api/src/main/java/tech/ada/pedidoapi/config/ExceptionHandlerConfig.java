package tech.ada.pedidoapi.config;

import jakarta.validation.UnexpectedTypeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import tech.ada.pedidoapi.exception.CustomerNotFoundException;
import tech.ada.pedidoapi.exception.InvalidAmountException;
import tech.ada.pedidoapi.exception.OrderNotFoundException;
import tech.ada.pedidoapi.exception.ProductNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleException(InvalidAmountException e) {
        String error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleException(ProductNotFoundException e) {
        String error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleException(CustomerNotFoundException e) {
        String error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleException(OrderNotFoundException e) {
        String error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }




}
