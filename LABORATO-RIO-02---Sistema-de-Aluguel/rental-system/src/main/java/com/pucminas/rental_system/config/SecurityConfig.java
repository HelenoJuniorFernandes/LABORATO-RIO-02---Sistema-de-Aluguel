package com.pucminas.rental_system.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Permite acesso ao H2 Console
                .requestMatchers(PathRequest.toH2Console()).permitAll() 
                // Permite acesso a recursos estáticos e páginas públicas
                .requestMatchers(AntPathRequestMatcher.antMatcher("/css/**"), AntPathRequestMatcher.antMatcher("/js/**"), AntPathRequestMatcher.antMatcher("/register"), AntPathRequestMatcher.antMatcher("/login")).permitAll()
                // Permissões de Agente
                .requestMatchers(AntPathRequestMatcher.antMatcher("/pedidos/evaluate/**"), AntPathRequestMatcher.antMatcher("/pedidos/pending")).hasRole("AGENTE")
                // Permissões de Cliente
                .requestMatchers(AntPathRequestMatcher.antMatcher("/pedidos/new"), AntPathRequestMatcher.antMatcher("/pedidos/mine")).hasRole("CLIENTE")
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/pedidos", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());
            
        // Desabilita CSRF para o H2 Console funcionar
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers(PathRequest.toH2Console())
        );
        
        // Permite que o H2 Console seja exibido em um frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
