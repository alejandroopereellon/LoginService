package es.alejandroperellon.LoginService.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.alejandroperellon.LoginService.usuarios.model.Usuario;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su nombre de usuario O por su correo electrónico. Se
	 * suele usar en el login cuando el campo de entrada puede contener cualquiera
	 * de los dos.
	 *
	 * @param nombreUsuario valor a comparar con el campo nombreUsuario
	 * @param correo        valor a comparar con el campo correo
	 * @return Optional con el usuario encontrado, o vacío si no existe
	 */
	public Optional<Usuario> findByCorreoOrNombreUsuario(String correo, String nombreUsuario);

}
