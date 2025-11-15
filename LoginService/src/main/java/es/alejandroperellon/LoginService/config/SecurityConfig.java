package es.alejandroperellon.LoginService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Parámetros recomendados por OWASP 2025
		int saltLength = 16; // 16 bytes
		int hashLength = 32; // 32 bytes
		int parallelism = 1; // un hilo
		int memory = 65536; // 64MB
		int iterations = 3;

		return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
	}
}
