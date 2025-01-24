package org.sid.orderservice.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    private JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //autoriser les requêtes HTTP sur les URL commençant par /api/** sans authentification
                .authorizeHttpRequests(ar -> ar
                        .requestMatchers("/h2-console/**","/swagger-ui.html","/v3/**","/swagger-ui/**","/api/orders/**").permitAll()
                )
                /*.authorizeHttpRequests(ar -> ar
                        .requestMatchers("/api/orders/**").hasAuthority("ADMIN"))*/
                //désactiver la protection contre les frames
                //l'ordre n'est pas important
                .headers(headers -> headers
                        .frameOptions(fo->fo.disable()))
                .authorizeHttpRequests(ar -> ar
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm->sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                //le securiser avec keycloak
                .oauth2ResourceServer(oauth2->oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .build();
    }
    //autoriser les requêtes HTTP sur les URL commençant par /api/** sans authentification
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //quelques soit la requête on va appliquer la configuration
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    //401 pour non authentifié
    //403 pour non autorisé (authentifié mais n'a pas les droits)
}
