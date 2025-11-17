package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoDireccionIp;

/**
 * Se encargar de buscar los baneos existentes de la BBDD
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public interface BaneoDireccionIpRepository extends JpaRepository<BaneoDireccionIp, Long> {

	/**
	 * Busca un registro por ip y fecha de fin antes de (ahora, fehca concreta...)
	 * 
	 * @param ip    es el ip que se va a banear
	 * @param ahora es el momento en que se va a comprobar si esta baneado
	 * @return {@link Baneo} que cumpla los requisitos
	 */
	Optional<BaneoDireccionIp> findByIpAndMomentoFinBaneoAfter(String ip, LocalDateTime ahora);

}
