package com.pedidos360.catalogo.messaging;

import com.pedidos360.catalogo.dto.ItemPedidoEventoDTO;
import com.pedidos360.catalogo.dto.PedidoEventoDTO;
import com.pedidos360.catalogo.service.ProductoStockService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Consumidor de eventos de mensajería asíncrona para el dominio de Catálogo / Inventario.
 * Escucha la cola 'pedidos.stock.queue', descuenta existencias de inventario de cada ítem
 * y gestiona confirmaciones explícitas (ACK manual) o desvíos a DLQ mediante basicNack.
 */
@Component
public class StockOrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(StockOrderConsumer.class);

    private final ProductoStockService stockService;

    public StockOrderConsumer(ProductoStockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Procesa el evento de pedido creado para descontar el stock disponible.
     * Maneja confirmación explícita mediante basicAck y enrutamiento a DLQ en caso de fallo.
     *
     * @param pedido Evento con datos del pedido y la lista de ítems adquiridos
     * @param channel Canal AMQP para confirmación manual
     * @param deliveryTag Identificador único del mensaje en el canal
     */
    @RabbitListener(
            queues = "${pedidos360.rabbitmq.queue.stock}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void procesarDescuentoStock(
            @Payload PedidoEventoDTO pedido,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        log.info("------------------------------------------------------------------");
        log.info(">>> [RABBITMQ CONSUMER - STOCK] Evento recibido para Pedido: {}", pedido.getPedidoId());
        log.info("Cliente: {}, Total: ${}, Total Ítems: {}",
                pedido.getClienteEmail(), pedido.getTotalPagar(),
                (pedido.getItems() != null ? pedido.getItems().size() : 0));

        try {
            if (pedido.getItems() == null || pedido.getItems().isEmpty()) {
                log.warn("El pedido {} no contiene ítems para descontar stock. Confirmando mensaje (ACK).", pedido.getPedidoId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            // Descontar stock para cada ítem adquirido
            for (ItemPedidoEventoDTO item : pedido.getItems()) {
                log.info("Descontando stock -> ProductoId: '{}', Cantidad: {}",
                        item.getProductoId(), item.getCantidad());
                stockService.descontarStock(item.getProductoId(), item.getCantidad());
            }

            log.info(">>> [STOCK OK] Todos los ítems fueron descontados exitosamente. Enviando ACK manual (deliveryTag={})", deliveryTag);
            channel.basicAck(deliveryTag, false);

        } catch (IllegalStateException e) {
            // Error de negocio: Stock insuficiente
            log.error(">>> [ERROR NEGOCIO - DLQ] Stock insuficiente para pedido {}: {}. Enviando NACK hacia DLQ.",
                    pedido.getPedidoId(), e.getMessage());
            // requeue = false permite que RabbitMQ envíe el mensaje al Dead Letter Exchange
            channel.basicNack(deliveryTag, false, false);

        } catch (Exception e) {
            // Error técnico o producto no encontrado
            log.error(">>> [ERROR TÉCNICO - DLQ] Fallo al procesar pedido {}: {}. Enviando NACK hacia DLQ.",
                    pedido.getPedidoId(), e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        } finally {
            log.info("------------------------------------------------------------------");
        }
    }
}
