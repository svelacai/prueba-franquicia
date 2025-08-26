package prototipe.franquicia.infrastructure.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaServicePort franquiciaService;

    @Autowired
    public FranquiciaController(FranquiciaServicePort franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping
    public Mono<Franquicia> agregarFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaService.agregarFranquicia(franquicia);
    }

    @PostMapping("/{franquiciaId}/sucursales")
    public Mono<Franquicia> agregarSucursal(@PathVariable Integer franquiciaId, @RequestBody Sucursal sucursal) {
        return franquiciaService.agregarSucursal(franquiciaId, sucursal);
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos")
    public Mono<Franquicia> agregarProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @RequestBody Producto producto) {
        return franquiciaService.agregarProducto(franquiciaId, sucursalNombre, producto);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}")
    public Mono<Franquicia> eliminarProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre) {
        return franquiciaService.eliminarProducto(franquiciaId, sucursalNombre, productoNombre);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock")
    public Mono<Franquicia> modificarStockProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre, @RequestBody Map<String, Integer> requestBody) {
        int nuevoStock = requestBody.get("nuevoStock");
        return franquiciaService.modificarStockProducto(franquiciaId, sucursalNombre, productoNombre, nuevoStock);
    }

    @GetMapping("/{franquiciaId}/productos-mas-stock")
    public Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(@PathVariable Integer franquiciaId) {
        return franquiciaService.getProductoConMasStockPorSucursal(franquiciaId);
    }

    @PutMapping("/{franquiciaId}/nombre")
    public Mono<Franquicia> actualizarNombreFranquicia(@PathVariable Integer franquiciaId, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/nombre")
    public Mono<Franquicia> actualizarNombreSucursal(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalNombre, nuevoNombre);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/nombre")
    public Mono<Franquicia> actualizarNombreProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalNombre, productoNombre, nuevoNombre);
    }
}