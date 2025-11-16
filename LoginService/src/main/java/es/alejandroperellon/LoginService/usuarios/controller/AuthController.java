package es.alejandroperellon.LoginService.usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.alejandroperellon.LoginService.clasesAxiliares.ApiResponse;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioLogin;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioPublico;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioRegistro;
import es.alejandroperellon.LoginService.usuarios.service.LoginService;
import es.alejandroperellon.LoginService.usuarios.service.RegisterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final LoginService loginService;
	private final RegisterService registerService;

	public AuthController(LoginService loginService, RegisterService registerService) {
		this.loginService = loginService;
		this.registerService = registerService;
	}

	/**
	 * Endpoint de inicio de sesión. Recibe los datos de login en formato JSON y
	 * devuelve los datos públicos del usuario junto con su token de sesión si las
	 * credenciales son correctas.
	 *
	 * POST /auth/login
	 */
	@PostMapping("/login")
	public ResponseEntity<DTOUsuarioPublico> login(@Valid @RequestBody DTOUsuarioLogin datosLogin) {
		DTOUsuarioPublico usuarioPublico = loginService.login(datosLogin);

		return ResponseEntity.ok(usuarioPublico);
	}

	/**
	 * Endpoint de registro de usuario. Recibe los datos de registro en formato JSON
	 * y devuelve una respuesta del servidor indicando si se ha registrado
	 * correctamente o los errores en el registro.
	 *
	 * POST /auth/register
	 */
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody DTOUsuarioRegistro datosRegistro) {
		ApiResponse respuesta = registerService.register(datosRegistro);

		return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

}
