package tech.ada.clienteapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.clienteapi.model.dto.ClienteRequest;
import tech.ada.clienteapi.model.dto.ClienteResponse;
import tech.ada.clienteapi.service.ClienteService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClienteResponse> save(@Valid @RequestBody ClienteRequest clienteRequest){
        return Mono.defer(() -> clienteService.save(clienteRequest))
                .map(ClienteResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping ("/{id}")
    public Mono<ClienteResponse> getClienteById(@PathVariable String id){
        return Mono.defer(() -> clienteService.getClienteById(id))
                .map(ClienteResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }
}
