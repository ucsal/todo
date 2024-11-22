package br.ucsal.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                . csrf(csrf -> csrf.disable()) // Desativa CSRF para simplificar (não recomendado em produção)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Permite rotas públicas
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas ADMIN pode acessar
                        .requestMatchers("/user/**").hasRole("USER")  // Apenas USER pode acessar
                        .anyRequest().authenticated() // Exige autenticação para todas as outras rotas
                )
                .formLogin(form -> form.permitAll()); // Habilita a página de login padrão do Spring Security

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encoder de senhas
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user1 = User.withUsername("user")
                .password(passwordEncoder().encode("password")) // Criptografa a senha
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, admin);
    }
}
