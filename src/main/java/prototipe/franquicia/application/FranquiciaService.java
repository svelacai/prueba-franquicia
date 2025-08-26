package prototipe.franquicia.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaRepositoryPort;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FranquiciaService implements FranquiciaServicePort {

	private final FranquiciaRepositoryPort franquiciaRepository;

	public FranquiciaService(FranquiciaRepositoryPort franquiciaRepository) {
		this.franquiciaRepository = franquiciaRepository;
	}

	@Override
	public Mono<Franquicia> agregarFranquicia(Franquicia franquicia) {
		return franquiciaRepository.save(franquicia);
	}

	@Override
	public Mono<Franquicia> agregarSucursal(Integer franquiciaId, Sucursal sucursal) {
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			franquicia.getSucursales().add(sucursal);
			return franquiciaRepository.save(franquicia);
		});
	}

	@Override
	public Mono<Franquicia> agregarProducto(Integer franquiciaId, String sucursalNombre, Producto producto) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				sucursalEncontrada.getProductos().add(producto);
				return franquiciaRepository.save(franquicia);
			}
			return Mono.empty();
		});
	}

	@Override
	public Mono<Franquicia> modificarStockProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			Integer nuevoStock) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				Producto productoEncontrado = sucursalEncontrada.getProductos().stream()
						.filter(p -> p.getNombre().equals(productoNombre)).findFirst().orElse(null);
				if (productoEncontrado != null) {
					productoEncontrado.setStock(nuevoStock);
					return franquiciaRepository.save(franquicia);
				}
			}
			return Mono.empty();
		});
	}

	@Override
	public Mono<Franquicia> eliminarProducto(Integer franquiciaId, String sucursalNombre, String productoNombre) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				sucursalEncontrada.getProductos().removeIf(p -> p.getNombre().equals(productoNombre));
				return franquiciaRepository.save(franquicia);
			}
			return Mono.empty();
		});
	}

	@Override
	public Flux<Producto> findProductosConMasStock(Integer franquiciaId) {
		return franquiciaRepository.findById(franquiciaId)
				.flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
				.flatMap(sucursal -> Flux.fromIterable(sucursal.getProductos())
						.sort(Comparator.comparing(Producto::getStock).reversed()).take(1));
	}

	@Override
	public Mono<Franquicia> actualizarNombreFranquicia(Integer franquiciaId, String nuevoNombre) {
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			franquicia.setNombre(nuevoNombre);
			return franquiciaRepository.save(franquicia);
		});
	}

	@Override
	public Mono<Franquicia> actualizarNombreSucursal(Integer franquiciaId, String sucursalNombre, String nuevoNombre) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				sucursalEncontrada.setNombre(nuevoNombre);
				return franquiciaRepository.save(franquicia);
			}
			return Mono.empty();
		});
	}

	@Override
	public Mono<Franquicia> actualizarNombreProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			String nuevoNombre) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				Producto productoEncontrado = sucursalEncontrada.getProductos().stream()
						.filter(p -> p.getNombre().equals(productoNombre)).findFirst().orElse(null);
				if (productoEncontrado != null) {
					productoEncontrado.setNombre(nuevoNombre);
					return franquiciaRepository.save(franquicia);
				}
			}
			return Mono.empty();
		});
	}

	@Override
	public Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(Integer franquiciaId) {
		return franquiciaRepository.findById(franquiciaId)
				.flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()).flatMap(sucursal -> {
					Producto productoMasStock = sucursal.getProductos().stream()
							.max(Comparator.comparing(Producto::getStock)).orElse(null);
					if (productoMasStock != null) {
						Map<String, Producto> resultado = new HashMap<>();
						resultado.put(sucursal.getNombre(), productoMasStock);
						return Mono.just(resultado);
					}
					return Mono.empty();
				}));
	}
}