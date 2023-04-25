package tech.ada.pedidoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.pedidoapi.client.CatalogoClient;
import tech.ada.pedidoapi.client.ClienteClient;
import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.client.dto.Produto;
import tech.ada.pedidoapi.exception.InvalidAmountException;
import tech.ada.pedidoapi.exception.OrderNotFoundException;
import tech.ada.pedidoapi.model.Item;
import tech.ada.pedidoapi.model.dto.ItemRequest;
import tech.ada.pedidoapi.model.Pedido;
import tech.ada.pedidoapi.model.Status;
import tech.ada.pedidoapi.model.dto.PedidoRequest;
import tech.ada.pedidoapi.repository.PedidoRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final ClienteClient clienteClient;

    public Mono<Pedido> efetuarPedido(PedidoRequest pedidoRequest) {
        log.info("Iniciando Pedido - {}", pedidoRequest);
        return Mono.defer(() -> verificarCliente(pedidoRequest.idCliente()))
                .flatMap(cliente -> {
                    log.info("Verificando estoque e calculando total do pedido");

                    Pedido pedido = Pedido.builder()
                            .id(UUID.randomUUID().toString())
                            .cliente(cliente)
                            .status(Status.REALIZADO)
                            .total(BigDecimal.ZERO)
                            .data(Instant.now())
                            .build();

                    List<Item> itens = new ArrayList<>();

                    return obterProdutos(pedidoRequest.itens())
                            .map(produto -> {
                                Integer qtd = pedidoRequest
                                        .itens()
                                        .stream()
                                        .filter(itemRequest -> itemRequest.idProduto().equals(produto.id()))
                                        .findFirst()
                                        .get()
                                        .quantidade();

                                itens.add(new Item(produto.id(), produto.nome(),
                                        qtd, produto.preco()));

                                BigDecimal quantidade = BigDecimal.valueOf(qtd);
                                return produto.preco().multiply(quantidade);
                            }).reduce(BigDecimal::add)
                            .map(bigDecimal -> {
                                pedido.setTotal(bigDecimal);
                                pedido.setItens(itens);
                                return pedido;
                            }).flatMap(__ -> pedidoRepository.save(pedido));
                });
    }

    private Mono<Cliente> verificarCliente(String idCliente) {
        return Mono.defer(() -> {
            log.info("Verificando Cliente - {}", idCliente);
            return clienteClient.getClienteById(idCliente);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<Produto> obterProdutos(List<ItemRequest> itens){
        return Flux.fromIterable(itens).
                subscribeOn(Schedulers.boundedElastic())
                .flatMap(itemRequest ->
                        catalogoClient.getProdutoById(itemRequest.idProduto())
                                .map(produto -> {
                                    if (produto.quantidade() < itemRequest.quantidade())
                                        throw new InvalidAmountException(produto);
                                    return produto;
                                }));
    }

    public Flux<Pedido> getAll() {
        return Flux.defer(() ->{
            log.info("Buscando todos os pedidos no banco de dados");
            return pedidoRepository.findAll();
        });
    }

    public Mono<Pedido> getPedidoById(String id) {
        return Mono.defer(() -> {
            log.info("Buscando Pedido no banco de dados - {}", id);
            return pedidoRepository.findById(id)
                    .switchIfEmpty(Mono.error(() -> {
                        log.error("Id de Pedido não encontrado. Digite um Id válido - {}", id);
                        return new OrderNotFoundException("Pedido não existe na base de dados");
                    }));
        });
    }
}