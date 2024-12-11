package br.usp.projetoa.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private UserAuthenticationFilter userAuthenticationFilter;

    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    public static final String[] ENDPOINTS_SEM_AUTENTICACAO = {
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",

        "/api/auth/login",
        "/api/alunos/register",
        "/api/docentes/register"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> request
                .requestMatchers(ENDPOINTS_SEM_AUTENTICACAO).permitAll()

                // Autorização referentes aos endpoints dos alunos
                .requestMatchers(HttpMethod.GET, "/api/alunos").hasAnyRole("ORIENTADOR", "CCP")
                .requestMatchers(HttpMethod.DELETE, "/api/alunos/**").hasAnyRole("ORIENTADOR", "CCP")

                // Autorização referentes aos endpoints dos docentes
                .requestMatchers(HttpMethod.GET, "/api/docentes").hasRole("CCP")
                .requestMatchers("/api/docentes/**").hasAnyRole("ORIENTADOR", "CCP")

                // Autorização referentes aos endpoints dos pareceres
                .requestMatchers("/api/pareceres/**").hasAnyRole("ORIENTADOR", "CCP")

                // Autorização referentes aos endpoints dos relatórios
                .requestMatchers(HttpMethod.GET, "/api/relatorios").hasAnyRole("ORIENTADOR", "CCP")
                .requestMatchers(HttpMethod.POST, "/api/relatorios").hasRole("ALUNO")
                .requestMatchers(HttpMethod.PUT, "/api/relatorios/**").hasRole("ALUNO")
                .requestMatchers("/api/relatorios/orientador/**").hasAnyRole("ORIENTADOR", "CCP")

                .anyRequest().authenticated()
            )
            .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Origem permitida (frontend)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Cabeçalhos permitidos
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Cabeçalhos expostos (ex.: token JWT)
        configuration.setAllowCredentials(true); // Permite envio de cookies ou credenciais

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração para todas as rotas
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
