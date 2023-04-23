package tech.ada.pedidoapi.pubsub;

import tech.ada.pedidoapi.model.Pedido;

public record PubSubMessage(Pedido pedido) {
}
