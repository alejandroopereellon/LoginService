package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.IntentoInicioSesion;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

public interface IntentoInicioSesionRepository extends JpaRepository<IntentoInicioSesion, Long> {

//	/**
//	 * Obtiene el ultimo intento de inicio de sesion exitoso desde la ip introducida
//	 * 
//	 * @param direccionIP es el direccion ip que se va a comprobar
//	 * @param resultado   tipo de resultado del intento
//	 * @return ultimo {@link IntentoInicioSesion} correcto desde esa direccion ip
//	 */
//	Optional<IntentoInicioSesion> findTopByDireccionIPAndResultadoOrderByMomentoIntentoDesc(String direccionIP,
//			ResultadoIntentoInicioSesion resultado);
//
//	/**
//	 * Obtiene el ultimo intento de inicio de sesion exitoso de el usuario
//	 * introducido
//	 * 
//	 * @param idUsuario es el usuario que se va a comprobar
//	 * @param resultado tipo de resultado del intento
//	 * @return ultimo {@link IntentoInicioSesion} correcto desde esa direccion ip
//	 */
//	Optional<IntentoInicioSesion> findTopByUsuarioIdAndResultadoOrderByMomentoIntentoDesc(Long idUsuario,
//			ResultadoIntentoInicioSesion resultado);

	/**
	 * Obtiene cantidad de intento de inicio de sesion fallidos desde la ip
	 * introducida
	 * 
	 * @param direccionIP es el direccion ip que se va a comprobar
	 * @param resultado   tipo de resultado del intento
	 * @param fecha       fecha desde la que se cuentan los intentos
	 * @return int con el numero de {@link IntentoInicioSesion} incorrectos desde
	 *         esa direccion ip
	 */
	int countByDireccionIPAndResultadoAndMomentoIntentoAfter(String direccionIP, ResultadoIntentoInicioSesion resultado,
			LocalDateTime fecha);

	/**
	 * Obtiene cantidad de intento de inicio de sesion fallidos desde el usuario
	 * introducido
	 * 
	 * @param idUsuario es el usuario que se va a comprobar
	 * @param resultado tipo de resultado del intento
	 * @param fecha     fecha desde la que se cuentan los intentos
	 * @return int con el numero de {@link IntentoInicioSesion} incorrectos desde
	 *         esa direccion ip
	 */
	int countByUsuarioAndResultadoAndMomentoIntentoAfter(Usuario usuario, ResultadoIntentoInicioSesion resultado,
			LocalDateTime fecha);

}
