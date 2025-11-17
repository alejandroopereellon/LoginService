package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.builder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.MotivosBaneo;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoDireccionIp;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoUsuario;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Construye un nuevo objeto {@link Baneo}
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class BaneoBuilder {
	private static final Logger logger = LoggerFactory.getLogger(BaneoBuilder.class);

	private final Clock clock;

	// Inyectamos el reloj para testear fácilmente (por defecto, sistema)
	public BaneoBuilder(Clock clock) {
		this.clock = clock;
	}

	/**
	 * Contruye un nuevo objeto {@link BaneoUsuario}
	 * 
	 * @param tiempoBaneo es el tiempo que se va a banear al cliente
	 * @param usuario     es el {@link Usuario} que se va a banear
	 * @param motivo      es el {@link MotivosBaneo} por el que se banea el usuario
	 * @return {@link BaneoUsuario} con el nuevo baneo creado
	 */
	public BaneoUsuario nuevoBaneoUsuario(int tiempoBaneo, Usuario usuario, MotivosBaneo motivo) {
		logger.debug("Se va a generar un nuevo baneo al usuario {}", usuario.getId());

		BaneoUsuario baneo = new BaneoUsuario();

		// Establecemos el tiempo de baneo
		LocalDateTime ahora = LocalDateTime.now(clock.withZone(ZoneOffset.UTC));
		LocalDateTime fin = ahora.plusMinutes(tiempoBaneo);

		baneo.setMomentoBaneo(ahora);
		baneo.setMonentoFinBaneo(fin);
		baneo.setMotivo(motivo);
		baneo.setUsuario(usuario);
		logger.debug("Se han completado los datos del baneo");

		logger.debug("Creado baneo de usuario id={} motivo={} hasta={}", usuario.getId(), motivo, fin);
		return baneo;
	}

	/**
	 * Contruye un nuevo objeto {@link BaneoDireccionIp}
	 * 
	 * @param tiempoBaneo es el tiempo que se va a banear al cliente
	 * @param direccionIp es la direccion ip que se va a banear
	 * @param motivo      es el {@link MotivosBaneo} por el que se banea el usuario
	 * @return {@link BaneoDireccionIp} con el nuevo baneo creado
	 */
	public BaneoDireccionIp nuevoBaneoDireccionIp(int tiempoBaneo, String direccionIp, MotivosBaneo motivo) {
		logger.debug("Se va a generar un nuevo baneo a la direccion ip {}", direccionIp);
		BaneoDireccionIp baneo = new BaneoDireccionIp();

		// Establecemos el tiempo de baneo
		LocalDateTime ahora = LocalDateTime.now(clock.withZone(ZoneOffset.UTC));
		LocalDateTime fin = ahora.plusMinutes(tiempoBaneo);

		baneo.setMomentoBaneo(ahora);
		baneo.setMonentoFinBaneo(fin);
		baneo.setIp(direccionIp);
		baneo.setMotivo(motivo);
		logger.debug("Se han completado los datos del baneo");

		logger.info("Creado baneo de IP={} motivo={} hasta={}", direccionIp, motivo, fin);
		return baneo;
	}
}
