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
 * Inicializado con catálogo oficial completo clasificado por categorías para Pedidos360 Store.
 */
@Service
public class ProductoStockService {

    private static final Logger log = LoggerFactory.getLogger(ProductoStockService.class);

    private final Map<String, Producto> inventario = new ConcurrentHashMap<>();

    @PostConstruct
    public void inicializarCatalogo() {
        // 1. LAPTOPS
        inventario.put("PROD-101", new Producto("PROD-101", "Laptop Asus ROG Strix G16", "Intel Core i9-13980HX, 32GB DDR5, 1TB SSD, RTX 4070 8GB, 165Hz QHD+", 1290000.0, 8, "Laptops"));
        inventario.put("PROD-102", new Producto("PROD-102", "Apple MacBook Pro 14\" M3 Pro", "Chip M3 Pro (11c CPU, 14c GPU), 18GB RAM, 512GB SSD, Liquid Retina XDR", 1890000.0, 5, "Laptops"));
        inventario.put("PROD-103", new Producto("PROD-103", "Lenovo Legion Pro 7i Gen 8", "Intel Core i9-13900HX, 32GB RAM, 1TB SSD, RTX 4080 12GB, 240Hz", 1690000.0, 6, "Laptops"));
        inventario.put("PROD-104", new Producto("PROD-104", "Dell XPS 15 9530 Creator Edition", "Intel Core i7-13700H, 32GB RAM, 1TB SSD, RTX 4060, Pantalla OLED 3.5K", 1540000.0, 4, "Laptops"));
        inventario.put("PROD-105", new Producto("PROD-105", "HP Omen 16 Transcend", "Intel Core i7-13700HX, 16GB DDR5, 1TB SSD, RTX 4060, Mini-LED 240Hz", 1190000.0, 9, "Laptops"));
        inventario.put("PROD-106", new Producto("PROD-106", "Acer Predator Helios 16", "Intel Core i7-13700HX, 32GB RAM, 1TB SSD, RTX 4070 8GB, 240Hz G-Sync", 1350000.0, 7, "Laptops"));
        inventario.put("PROD-107", new Producto("PROD-107", "Razer Blade 15 Advanced", "Intel Core i9-13800H, 32GB DDR5, 1TB SSD, RTX 4070, QHD 240Hz", 2190000.0, 3, "Laptops"));

        // 2. PERIFÉRICOS
        inventario.put("PROD-201", new Producto("PROD-201", "Mouse Logitech G Pro X Superlight 2", "Sensor HERO 2 con 32K DPI, switches óptico-mecánicos, 60g ultraligero", 129990.0, 15, "Periféricos"));
        inventario.put("PROD-202", new Producto("PROD-202", "Teclado Mecánico Keychron Q1 Pro", "Distribución 75% aluminio CNC, switches mecánicos lubricados, Bluetooth 5.1", 169990.0, 9, "Periféricos"));
        inventario.put("PROD-203", new Producto("PROD-203", "Mouse Razer DeathAdder V3 Pro", "Sensor óptico Focus Pro 30K, 63g de peso, switches Gen-3", 119990.0, 12, "Periféricos"));
        inventario.put("PROD-204", new Producto("PROD-204", "Teclado SteelSeries Apex Pro TKL", "Switches magnéticos ajustables OmniPoint 2.0 con pantalla OLED", 189990.0, 7, "Periféricos"));
        inventario.put("PROD-205", new Producto("PROD-205", "Controlador Elgato Stream Deck MK.2", "15 teclas LCD personalizables para streaming y control de estudio", 139990.0, 11, "Periféricos"));
        inventario.put("PROD-206", new Producto("PROD-206", "Mouse Logitech MX Master 3S", "Sensor Darkfield 8K DPI para cristal, scroll MagSpeed electromagnético", 89990.0, 18, "Periféricos"));
        inventario.put("PROD-207", new Producto("PROD-207", "Mousepad Artisan Ninja FX Hayate Otsu XL", "Superficie jacquard híbrida japonesa para esports con base Poron", 54990.0, 14, "Periféricos"));

        // 3. MONITORES
        inventario.put("PROD-301", new Producto("PROD-301", "Monitor Gamer Samsung Odyssey G7 27\"", "WQHD (2560x1440) 240Hz 1ms, panel curvo 1000R QLED HDR600", 429990.0, 6, "Monitores"));
        inventario.put("PROD-302", new Producto("PROD-302", "Monitor LG UltraGear 27\" OLED 240Hz", "Panel OLED QHD con 0.03ms GtG, 98.5% DCI-P3, HDMI 2.1", 649990.0, 4, "Monitores"));
        inventario.put("PROD-303", new Producto("PROD-303", "Monitor ASUS ROG Swift 32\" 4K Mini-LED", "4K UHD 144Hz, retroiluminación Mini-LED 1152 zonas, DisplayHDR 1400", 1199900.0, 3, "Monitores"));
        inventario.put("PROD-304", new Producto("PROD-304", "Monitor Dell Alienware 34\" QD-OLED", "Curvo 1800R UltraWide WQHD (3440x1440) 165Hz, Quantum Dot OLED", 849990.0, 5, "Monitores"));
        inventario.put("PROD-305", new Producto("PROD-305", "Monitor BenQ MOBIUZ EX2710Q 27\"", "Panel IPS 2K QHD 165Hz, audio treVolo 2.1 con subwoofer de 5W", 299990.0, 8, "Monitores"));
        inventario.put("PROD-306", new Producto("PROD-306", "Monitor Gigabyte M27Q X 27\" 240Hz KVM", "Super Speed IPS QHD 240Hz con switch KVM integrado para dos PCs", 369990.0, 10, "Monitores"));

        // 4. AUDIO
        inventario.put("PROD-401", new Producto("PROD-401", "Audífonos Sony WH-1000XM5 Wireless", "Cancelación activa de ruido líder, procesador V1/QN1, 30h de batería", 279990.0, 14, "Audio"));
        inventario.put("PROD-402", new Producto("PROD-402", "Audífonos HyperX Cloud III Wireless", "Batería de hasta 120 horas, audio espacial DTS Headphone:X, 53mm", 139990.0, 16, "Audio"));
        inventario.put("PROD-403", new Producto("PROD-403", "Audífonos SteelSeries Arctis Nova Pro", "Audio Hi-Res, estación base OLED, batería intercambiable en caliente", 329990.0, 6, "Audio"));
        inventario.put("PROD-404", new Producto("PROD-404", "Micrófono Shure SM7B Dinámico", "Estándar de radiodifusión, patrón cardioide con blindaje EMI", 369990.0, 8, "Audio"));
        inventario.put("PROD-405", new Producto("PROD-405", "Audífonos Audio-Technica ATH-M50xBT2", "Drivers 45mm de apertura amplia, DAC AK4331, 50h de uso", 189990.0, 11, "Audio"));
        inventario.put("PROD-406", new Producto("PROD-406", "Audífonos Sennheiser HD 660S2 Open-Back", "Audífonos de referencia para audiófilos, 300 Ohms, escena abierta", 499990.0, 4, "Audio"));

        // 5. ALMACENAMIENTO
        inventario.put("PROD-501", new Producto("PROD-501", "SSD Samsung 990 PRO 2TB PCIe 4.0", "Lectura secuencial 7.450 MB/s, NVMe 2.0 M.2 2280 con níquel térmico", 179990.0, 18, "Almacenamiento"));
        inventario.put("PROD-502", new Producto("PROD-502", "SSD Kingston FURY Renegade 4TB con Disipador", "Controlador Phison E18, 7.300 MB/s, disipador de aluminio y grafeno", 349990.0, 7, "Almacenamiento"));
        inventario.put("PROD-503", new Producto("PROD-503", "SSD WD_BLACK SN850X 2TB NVMe", "Velocidad de hasta 7.300 MB/s, Game Mode 2.0 con baja latencia", 169990.0, 14, "Almacenamiento"));
        inventario.put("PROD-504", new Producto("PROD-504", "Memoria RAM Corsair Dominator DDR5 32GB (2x16GB)", "DDR5 6000 MT/s CL30, refrigeración DHX, perfiles XMP 3.0 & EXPO", 189990.0, 10, "Almacenamiento"));
        inventario.put("PROD-505", new Producto("PROD-505", "SSD Portátil SanDisk Extreme PRO 2TB USB 3.2", "2.000 MB/s de transferencia, chasis de aluminio forjado resistente IP55", 219990.0, 9, "Almacenamiento"));
        inventario.put("PROD-506", new Producto("PROD-506", "Tarjeta MicroSDXC SanDisk Extreme 512GB V30", "190 MB/s lectura, clasificación A2 para Nintendo Switch, drones y 4K", 49990.0, 22, "Almacenamiento"));

        log.info("Catálogo oficial Pedidos360 inicializado con {} productos en 5 categorías", inventario.size());
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(inventario.values());
    }

    public Optional<Producto> buscarPorId(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(inventario.get(id.trim().toUpperCase()));
    }

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
