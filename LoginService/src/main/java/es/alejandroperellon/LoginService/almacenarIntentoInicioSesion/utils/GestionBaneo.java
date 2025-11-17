package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.utils;

import java.time.LocalDateTime;
import es.alejandroperellon.LoginService.usuarios.repository.UsuariosRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.MotivosBaneo;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.builder.BaneoBuilder;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoDireccionIp;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.BaneoUsuario;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.BaneoDireccionIpRepository;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.BaneoUsuarioRepository;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.IntentoInicioSesionRepository;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Gestiona todas las comprobaciones y corrrespondientes gestiones de baneo de
 * los usuarios
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
@Component
public class GestionBaneo {

	private static final Logger logger = LoggerFactory.getLogger(GestionBaneo.class);

	private final IntentoInicioSesionRepository inicioSesionRepository;
	private final BaneoDireccionIpRepository baneoDireccionIpRepository;
	private final BaneoUsuarioRepository baneoUsuarioRepository;

	public GestionBaneo(IntentoInicioSesionRepository inicioSesionRepository,
			BaneoDireccionIpRepository baneoDireccionIpRepository, BaneoUsuarioRepository baneoUsuarioRepository,
			UsuariosRepository usuariosRepository) {
		this.inicioSesionRepository = inicioSesionRepository;
		this.baneoDireccionIpRepository = baneoDireccionIpRepository;
		this.baneoUsuarioRepository = baneoUsuarioRepository;
	}

	/**
	 * Comprueba si hay que realizar algun tipo de baneo ya sea por usuario o por
	 * direccion ip.
	 * 
	 * El sistema tiene en cuenta que un usuario puede iniciar sesion desde varias
	 * IP y tambien tiene en cuenta que vaias IP pueden iniciar varios usuarios, por
	 * tanto banea de manera independiente ambos sistemas
	 * 
	 * @param usuario     es el {@link Usuario} que se va a banear
	 * @param direccionIp es la direccion ip que se va a banear
	 */
	public void comprobarCandidaturaBaneo(Usuario usuario, String direccionIp) {
		logger.info("Se va a comprobar si el usuario o direccion IP es candidato para baneo");

		// Comprobamos si la direccion ip es apta para baneo
		baneoIP(direccionIp);

		// Comprobamos si el usuario es apto para baneo
		baneoUsuario(usuario);

		logger.info("Se ha finalizado la comprobacion de si el usuario o direccion ip son candidatas para baneo");
	}

	/**
	 * Comprueba el numero de intentos desde la ultima conexion correcta en esa
	 * direccion IP, en caso de ser divisor de 10 se va a realizar un baneo de un
	 * tiempo total de numero de intentos de inicio de sesion en esa ip
	 * 
	 * @param direccionIp es la direccion ip que se va a comprobar
	 */
	@Transactional
	private void baneoIP(String direccionIp) {
		logger.debug("Se va a comprobar si la direccion ip {} es candidata a baneo", direccionIp);

		// Obtenemos el numero de baneos de una direccion ip
		int numeroBaneos = inicioSesionRepository.countByDireccionIPAndResultadoAndMomentoIntentoAfter(direccionIp,
				ResultadoIntentoInicioSesion.CORRECTO, LocalDateTime.now());
		logger.debug("Se han producido {} intentos de inicio de sesion incorrectos desde el ultimo acceso correcto");

		// Calculamos el numero de baneos
		if (numeroBaneos % 10 == 0) {
			// Generamos un nuevo baneo con el numero de minutos por cantidad de fallos si
			// es divisor de 10
			BaneoDireccionIp baneo = new BaneoBuilder().nuevoBaneoDireccionIp(numeroBaneos, direccionIp,
					MotivosBaneo.EXCESO_INTENTOS_FALLIDOS);
			logger.info("Se ha generado un baneo de {} minutos por la direccion IP {}", numeroBaneos, direccionIp);

			// Almacenamos el baneo en el sistema
			baneoDireccionIpRepository.save(baneo);
			logger.debug("Se ha almacenado el baneo de la direccion IP {}", direccionIp);
		} else {
			logger.debug("No se va a generar ningun baneo para la direccion IP {}", direccionIp);
		}
		
		logger.debug("Se ha finalizado la comprobacion del baneo para la direccion IP {}", direccionIp);
	}

	@Transactional
	private void baneoUsuario(Usuario usuario) {
		logger.info("Se va a comprobar si el usuario es apto para un baneo");

		// Comprobamos si el usuario no existe
		if (usuario == null) {
			logger.info("El usuario es nulo, no se comprobara nada");
			return;
		}

		// Obtenemos el numero de baneos de un usuario
		int numeroBaneos = inicioSesionRepository.countByUsuarioAndResultadoAndMomentoIntentoAfter(usuario,
				ResultadoIntentoInicioSesion.CORRECTO, LocalDateTime.now());

		logger.debug(
				"El usuario tiene un total de {} intentos de inicio de sesion incorrectos desde el ultimo acceso correcto",
				numeroBaneos);

		// Generamos un nuevo baneo con el numero de minutos por cantidad de fallos si
		// es divisor de 10
		if (numeroBaneos % 10 == 0) {
			BaneoUsuario baneo = new BaneoBuilder().nuevoBaneoUsuario(numeroBaneos, usuario,
					MotivosBaneo.EXCESO_INTENTOS_FALLIDOS);
			logger.info("Se ha generado un baneo de {} minutos por el usuario {}", numeroBaneos, usuario.getId());

			// Almacenamos el baneo en el sistema
			baneoUsuarioRepository.save(baneo);
			logger.debug("Se ha almacenado el baneo del usuario {}", usuario.getId());
		} else {
			logger.debug("No se va a generar ningun baneo para el usuario {}", usuario.getId());
		}

		logger.debug("Se ha finalizado la comprobacion del baneo del usuario {}", usuario.getId());
	}

}
