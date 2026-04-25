package com.pucminas.rental_system.service;

import com.pucminas.rental_system.model.User;
import com.pucminas.rental_system.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Singleton
public class UserDetailsServiceImpl implements AuthenticationProvider<HttpRequest<?>> {

    @Inject
    private UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        String email = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();
        
        User user = userRepository.findByEmail(email);
        
        // Verifica se o usuário existe e se a senha está correta
        // (Nota: Num ambiente de produção, use um PasswordEncoder como o BCrypt aqui!)
        if (user != null && user.getPassword().equals(password)) {
            return Mono.just(AuthenticationResponse.success(
                user.getEmail(), 
                Collections.singleton(user.getRole())
            ));
        }
        
        return Mono.just(AuthenticationResponse.failure("Credenciais inválidas"));
    }
}
