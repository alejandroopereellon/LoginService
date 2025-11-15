package es.alejandroperellon.LoginService.usuarios.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenPublicoBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioPublico;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Construye un {@link DTOUsuarioPublico} a partir de un {@link Usuario} y su
 * {@link Token}. El DTO contiene únicamente los datos aptos para ser devueltos
 * al cliente.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class BuilderDTOUsuarioPublico {

	private static final Logger logger = LoggerFactory.getLogger(BuilderDTOUsuarioPublico.class);

	private final Usuario usuario;
	private final Token token;

	public BuilderDTOUsuarioPublico(Usuario usuario, Token token) {
		this.usuario = usuario;
		this.token = token;
	}

	/**
	 * Copia los datos visibles del {@link Usuario} y el token público asociado al
	 * DTO.
	 *
	 * @return DTO con la información pública del usuario y su token.
	 */
	public DTOUsuarioPublico construirUsuarioPublico() {
		logger.info("Se va a construir el usuario publico del usuario {}", usuario);
		DTOUsuarioPublico usuarioPublico = new DTOUsuarioPublico();

		// Establecemos los datos del nuevo usuario publico
		usuarioPublico.setApellido(usuario.getApellido());
		logger.debug("Se ha establecido el apellido {}", usuario.getApellido());

		usuarioPublico.setApodo(usuario.getApodo());
		logger.debug("Se ha establecido el apodo {}", usuario.getApodo());

		usuarioPublico.setCorreoElectronico(usuario.getCorreo());
		logger.debug("Se ha establecido el correo electronico {}", usuario.getCorreo());

		usuarioPublico.setNombre(usuario.getNombre());
		logger.debug("Se ha establecido el nombre {}", usuario.getNombre());

		usuarioPublico.setNombreUsuario(usuario.getNombreUsuario());
		logger.debug("Se ha establecido el nombre de usuario {}", usuario.getNombreUsuario());

		usuarioPublico.setToken(new TokenPublicoBuilder().construirTokenPublico(token));
		logger.debug("Se ha establecido el token publico para el usuario");

		// Retornamos el usuario publico
		logger.info("Se va a retornar el usuario publico con correo electronico {}",
				usuarioPublico.getCorreoElectronico());
		return usuarioPublico;
	}

}
