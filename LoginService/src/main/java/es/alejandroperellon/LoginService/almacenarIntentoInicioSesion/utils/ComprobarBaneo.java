package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.utils;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoDireccionIp;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoUsuario;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.IntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.BaneoDireccionIpRepository;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.BaneoUsuarioRepository;
import es.alejandroperellon.LoginService.usuarios.excepciones.banException.DireccionIPBaneadaException;
import es.alejandroperellon.LoginService.usuarios.excepciones.banException.UsuarioBaneadoException;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Comprueba si existe restricciones de baneo de cualquier tipo
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
@Component
public class ComprobarBaneo {
	private static final Logger logger = LoggerFactory.getLogger(ComprobarBaneo.class);

	private BaneoUsuarioRepository baneoUsuarioRepository;
	private BaneoDireccionIpRepository baneoDireccionIpRepository;

	public ComprobarBaneo(BaneoUsuarioRepository baneoUsuarioRepository,
			BaneoDireccionIpRepository baneoDireccionIpRepository) {
		super();
		this.baneoUsuarioRepository = baneoUsuarioRepository;
		this.baneoDireccionIpRepository = baneoDireccionIpRepository;
	}

//	/**
//	 * Comprueba si el usuario o la direccion ip estan baneadas
//	 * 
//	 * @param usuario     es el {@link Usuario} que se va a comprobar
//	 * @param direccionIp es la direccion ip que se va a comprobar
//	 */
//	public void UsuarioIP(Usuario usuario, String direccionIp) {
//		logger.debug("Se va a comprobar si existe baneo aplicado al usuario {} o la direccion ip {}", usuario.getId(),
//				direccionIp);
//
//		// Comprobamos si el usuario esta baneado
//		comprobarBaneoActivoUsuario(usuario);
//		// Comprobamos si la direccion ip esta baneada
//		comprobarBaneoActivoIp(direccionIp);
//	}

	/**
	 * Comprueba si la direccion ip esta bloqueada
	 * 
	 * @param direccionIpm es la direccion IP que se va a comprobar
	 * @throws DireccionIPBaneadaException si existe alguna entrada activa
	 */
	public void baneoActivoIp(String direccionIp, IntentoInicioSesion intento) {
		logger.debug("Se van a comprbar los baneos por direccion IP {}", direccionIp);
		Optional<BaneoDireccionIp> baneo = baneoDireccionIpRepository.findByIpAndFechaFinAfter(direccionIp,
				LocalDateTime.now());
		if (baneo.isEmpty()) {
			logger.info("La direccion IP {} esta baneada desde {} hasta {}", direccionIp, baneo.get().getMomentoBaneo(),
					baneo.get().getMonentoFinBaneo());
			intento.setResultado(ResultadoIntentoInicioSesion.DIRECCION_IP_BANEADA);
			logger.debug("Se ha marcado el intento de inicio de sesion como DIRECCION_IP_BANEADA");
			throw new DireccionIPBaneadaException();
		}

		logger.debug("La direccion IP {} no contiene ningun baneo", direccionIp);
	}

	/**
	 * Comprueba si el usuario esta bloqueado
	 * 
	 * @param usuario es el usuario que se va a comprobar
	 * @throws UsuarioBaneadoException si existe alguna entrada activa
	 */
	public void baneoActivoUsuario(Usuario usuario, IntentoInicioSesion intento) {
		logger.debug("Se van a comprbar los baneos para el usuario {}", usuario.getId());
		Optional<BaneoUsuario> baneo = baneoUsuarioRepository.findByUsuarioAndFechaFinAfter(usuario,
				LocalDateTime.now());
		if (baneo.isEmpty()) {
			logger.info("El usuario {} esta baneada desde {} hasta {}", usuario.getId(), baneo.get().getMomentoBaneo(),
					baneo.get().getMonentoFinBaneo());
			intento.setResultado(ResultadoIntentoInicioSesion.USUARIO_BANEADO);
			logger.debug("Se ha marcado el intento de inicio de sesion como USUARIO_BANEADO");
			throw new UsuarioBaneadoException();
		}

		logger.debug("El usuario {} no contiene ningun baneo", usuario.getId());
	}
}
