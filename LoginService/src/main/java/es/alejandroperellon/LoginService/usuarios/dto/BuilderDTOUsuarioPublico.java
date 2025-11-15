package es.alejandroperellon.LoginService.usuarios.dto;

import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenPublicoBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
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

	private Usuario usuario;
	private Token token;

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
		DTOUsuarioPublico usuarioPublico = new DTOUsuarioPublico();

		// Establecemos los datos del nuevo usuario publico
		usuarioPublico.setApellido(usuario.getApellido());
		usuarioPublico.setApodo(usuario.getApodo());
		usuarioPublico.setCorreoElectronico(usuario.getCorreo());
		usuarioPublico.setNombre(usuario.getNombre());
		usuarioPublico.setNombreUsuario(usuario.getNombreUsuario());
		usuarioPublico.setToken(new TokenPublicoBuilder().construirTokenPublico(token));

		// Retornamos el usuario publico
		return usuarioPublico;
	}

}
