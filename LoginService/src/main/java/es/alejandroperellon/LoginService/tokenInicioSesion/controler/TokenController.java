package es.alejandroperellon.LoginService.tokenInicioSesion.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.alejandroperellon.LoginService.clasesAxiliares.ApiResponse;
import es.alejandroperellon.LoginService.tokenInicioSesion.dto.DTOTokenPublico;
import es.alejandroperellon.LoginService.tokenInicioSesion.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/token")
public class TokenController {

	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		super();
		this.tokenService = tokenService;
	}

	/**
	 * Endpoint de actualizacion de token corto publico. Recibe los datos del token
	 * antiguo en formato JSON y devuelve un nuevo token actualizado
	 * 
	 * POST /token/refresh
	 */
	@PostMapping("/refresh")
	public ResponseEntity<DTOTokenPublico> refreshShortToken(@Valid @RequestBody DTOTokenPublico tokenPublico) {
		DTOTokenPublico tokenPublicoNuevo = tokenService.actualizarToken(tokenPublico);

		return ResponseEntity.ok(tokenPublicoNuevo);
	}

	/**
	 * Endpoint para forzar la actualizacion de token corto publico. Recibe el token
	 * publico caducado y el token largo vigente para forzar la actualizacion de un
	 * token publico
	 * 
	 * POST /token/refresh
	 */
	@PostMapping("/forcerefresh")
	public ResponseEntity<DTOTokenPublico> forceRefreshShortToken(@Valid @RequestBody DTOTokenPublico tokenPublicoCorto,
			@Valid @RequestBody DTOTokenPublico tokenPublicoLargo) {
		DTOTokenPublico tokenPublicoNuevo = tokenService.forzarActualizarToken(tokenPublicoCorto, tokenPublicoLargo);

		return ResponseEntity.ok(tokenPublicoNuevo);
	}

	/**
	 * Endpoint de logout de usuario. Recibe los datos del token corto y largo
	 * publico en formato JSON y devuelve una respuesta del servidor indicando si se
	 * ha cerrado la sesion correctamente
	 *
	 * POST /token/logout
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout(@Valid @RequestBody DTOTokenPublico tokenPublicoCorto,
			@Valid @RequestBody DTOTokenPublico tokenPublicoLargo) {
		ApiResponse respuesta = tokenService.cerrarSesion(tokenPublicoCorto, tokenPublicoLargo);

		return ResponseEntity.ok(respuesta);
	}

}
