package com.pedidos360.catalogo.controller;

import com.pedidos360.catalogo.model.Producto;
import com.pedidos360.catalogo.service.ProductoStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private final ProductoStockService stockService;

    public ProductoController(ProductoStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Microservicio de Catálogo en ejecución con integración RabbitMQ");
        response.put("version", "2.0.0");
        return response;
    }

    /**
     * Lista todos los productos del catálogo con su stock disponible en tiempo real.
     * GET /api/productos
     */
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(stockService.listarProductos());
    }

    /**
     * Consulta el detalle y stock de un producto específico.
     * GET /api/productos/{id}
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable String id) {
        return stockService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "ERROR", "message", "Producto no encontrado: " + id)));
    }

    @PostMapping("/productos")
    public ResponseEntity<Map<String, String>> crearProducto(@RequestBody(required = false) String producto) {
        Map<String, String> response = new HashMap<>();

        if (producto == null || producto.trim().isEmpty()) {
            response.put("status", "ERROR");
            response.put("message", "El nombre del producto es obligatorio y no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String productoProcesado = producto.trim().toUpperCase();
        String nuevoId = "PROD-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        stockService.agregarOActualizar(new Producto(nuevoId, productoProcesado, "Producto nuevo", 10000.0, 10, "General"));

        response.put("status", "CREATED");
        response.put("id", nuevoId);
        response.put("producto", productoProcesado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
