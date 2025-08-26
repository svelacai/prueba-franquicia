package prototipe.franquicia.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FranquiciaServiceTest {

	@Mock
	private FranquiciaRepositoryPort franquiciaRepository;

	@InjectMocks
	private FranquiciaService franquiciaService;

	private Franquicia franquicia;
	private Sucursal sucursal;

	@BeforeEach
	void setUp() {
		franquicia = new Franquicia();
		franquicia.setId(1);
		franquicia.setNombre("Franquicia de Prueba");
		franquicia.setSucursales(new ArrayList<>());

		sucursal = new Sucursal();
		sucursal.setNombre("Sucursal Test");
		sucursal.setProductos(new ArrayList<>());
	}

	@Test
	void testAgregarFranquicia() {
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarFranquicia(franquicia))
				.expectNextMatches(f -> f.getNombre().equals("Franquicia de Prueba")).verifyComplete();
	}

	@Test
	void testAgregarSucursal() {
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarSucursal(1, sucursal)).expectNextMatches(
				f -> f.getSucursales().size() == 1 && f.getSucursales().get(0).getNombre().equals("Sucursal Test"))
				.verifyComplete();
	}
}