package tech.ada.pedidoapi.model.dto;

import lombok.Builder;
import lombok.Data;
import tech.ada.pedidoapi.client.dto.Cliente;
import tech.ada.pedidoapi.model.Item;
import tech.ada.pedidoapi.model.Pedido;
import tech.ada.pedidoapi.model.Status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Data
public class PedidoResponse {
        private String id;
        private Cliente cliente;
        private List<Item> itens;
        private Instant data;
        private Status status;
        private BigDecimal total;


        public static PedidoResponse convertToResponse(Pedido pedido){
                return PedidoResponse.builder()
                        .id(pedido.getId())
                        .cliente(pedido.getCliente())
                        .itens(pedido.getItens())
                        .data(pedido.getData())
                        .status(pedido.getStatus())
                        .total(pedido.getTotal())
                        .build();
        }
}
