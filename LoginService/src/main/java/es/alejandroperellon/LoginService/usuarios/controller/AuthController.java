package es.alejandroperellon.LoginService.usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioLogin;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioPublico;
import es.alejandroperellon.LoginService.usuarios.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * Endpoint de inicio de sesión. Recibe los datos de login en formato JSON y
	 * devuelve los datos públicos del usuario junto con su token de sesión si las
	 * credenciales son correctas.
	 *
	 * POST /auth/login
	 */
	@PostMapping("/login")
	public ResponseEntity<DTOUsuarioPublico> login(@RequestBody DTOUsuarioLogin datosLogin) {
		DTOUsuarioPublico usuarioPublico = authService.login(datosLogin);

		return ResponseEntity.ok(usuarioPublico);
	}

}
