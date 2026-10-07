package com.pedidos360.catalogo.service;

import com.pedidos360.catalogo.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio encargado de la gestión transaccional de stock e inventario de catálogo.
 */
@Service
public class ProductoStockService {

    private static final Logger log = LoggerFactory.getLogger(ProductoStockService.class);

    private final Map<String, Producto> inventario = new ConcurrentHashMap<>();

    @PostConstruct
    public void inicializarCatalogo() {
        inventario.put("PROD-001", new Producto("PROD-001", "Laptop Gamer Pro", "Intel Core i7 13th Gen, 16GB RAM, RTX 4060", 990000.0, 15, "Computación"));
        inventario.put("PROD-002", new Producto("PROD-002", "Monitor Curvo 27'' 165Hz", "Panel VA QHD 2K, 1ms, FreeSync Premium", 280000.0, 25, "Monitores"));
        inventario.put("PROD-003", new Producto("PROD-003", "Teclado Mecánico RGB", "Switches Red silenciosos, formato TKL", 65000.0, 40, "Accesorios"));
        inventario.put("PROD-004", new Producto("PROD-004", "Mouse Inalámbrico Ultra", "Sensor óptico 26K DPI, peso 58g", 45000.0, 50, "Accesorios"));
        inventario.put("PROD-005", new Producto("PROD-005", "Audífonos Gamer 7.1", "Sonido envolvente, cancelación de ruido", 75000.0, 30, "Audio"));
        log.info("Catálogo de productos inicializado con {} ítems y stock predeterminado", inventario.size());
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(inventario.values());
    }

    public Optional<Producto> buscarPorId(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(inventario.get(id.trim().toUpperCase()));
    }

    /**
     * Descuenta stock de un producto ante una compra confirmada.
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad a descontar
     * @return true si se descontó correctamente
     * @throws IllegalStateException si el stock es insuficiente
     * @throws NoSuchElementException si el producto no existe
     */
    public synchronized boolean descontarStock(String productoId, int cantidad) {
        if (productoId == null || cantidad <= 0) {
            throw new IllegalArgumentException("Parámetros de descuento de stock inválidos");
        }

        String idNormalizado = productoId.trim().toUpperCase();
        Producto producto = inventario.get(idNormalizado);

        if (producto == null) {
            throw new NoSuchElementException("Producto con ID " + productoId + " no encontrado en el catálogo");
        }

        if (producto.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para '" + producto.getNombre() + "'. Solicitado: "
                    + cantidad + ", Disponible: " + producto.getStock());
        }

        int stockPrevio = producto.getStock();
        producto.setStock(stockPrevio - cantidad);
        log.info("Stock actualizado para '{}' ({}): {} -> {} unidades",
                producto.getNombre(), idNormalizado, stockPrevio, producto.getStock());
        return true;
    }

    public void agregarOActualizar(Producto producto) {
        if (producto != null && producto.getId() != null) {
            inventario.put(producto.getId().trim().toUpperCase(), producto);
        }
    }
}
