package prototipe.franquicia.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Mono;

@WebFluxTest(FranquiciaController.class)
public class FranquiciaControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private FranquiciaServicePort franquiciaService;

	@Test
	void testAgregarFranquicia() {
		Franquicia franquicia = new Franquicia();
		franquicia.setId(1);
		franquicia.setNombre("Franquicia ABC");

		when(franquiciaService.agregarFranquicia(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		webTestClient.post().uri("/api/franquicias").contentType(MediaType.APPLICATION_JSON).bodyValue(franquicia)
				.exchange().expectStatus().isOk() 
				.expectBody().json("{\"id\":1, \"nombre\":\"Franquicia ABC\", \"sucursales\":null}"); 
	}
}