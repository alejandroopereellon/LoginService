package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoUsuario;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

public interface BaneoUsuarioRepository extends JpaRepository<BaneoUsuario, Long> {

	/**
	 * Busca un registro por usuario y fecha de fin antes de (ahora, fehca
	 * concreta...)
	 * 
	 * @param usuario es el usuario que se va a banear
	 * @param ahora   es el momento en que se va a comprobar si esta baneado
	 * @return {@link Baneo} que cumpla los requisitos
	 */
	Optional<BaneoUsuario> findByUsuarioAndMomentoFinBaneoAfter(Usuario usuario, LocalDateTime ahora);
}
