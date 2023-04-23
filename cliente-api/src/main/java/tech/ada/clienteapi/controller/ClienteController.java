package tech.ada.clienteapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.clienteapi.model.Cliente;
import tech.ada.clienteapi.model.dto.ClienteRequest;
import tech.ada.clienteapi.service.ClienteService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public Mono<Cliente> save(@Valid @RequestBody ClienteRequest clienteRequest){
        return Mono.defer(() -> clienteService.save(clienteRequest))
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping ("/{id}")
    public Mono<Cliente> getClienteById(@PathVariable String id){
        return Mono.defer(() -> clienteService.getClienteById(id))
                .subscribeOn(Schedulers.parallel());
    }
}
