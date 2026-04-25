package com.pucminas.rental_system.service;

import com.pucminas.rental_system.model.User;
import com.pucminas.rental_system.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Collections;

@Singleton
public class UserDetailsServiceImpl<B> implements HttpRequestAuthenticationProvider<B> {

    @Inject
    private UserRepository userRepository;

    @Override
    public AuthenticationResponse authenticate(HttpRequest<B> httpRequest,
                                               AuthenticationRequest<String, String> authenticationRequest) {
        String email = authenticationRequest.getIdentity();
        String password = authenticationRequest.getSecret();
        
        User user = userRepository.findByEmail(email);
        
        // Verifica se o usuário existe e se a senha está correta
        // (Nota: Num ambiente de produção, use um PasswordEncoder como o BCrypt aqui!)
        if (user != null && user.getPassword().equals(password)) {
            return AuthenticationResponse.success(
                user.getEmail(), 
                Collections.singleton(user.getRole())
            );
        }
        
        return AuthenticationResponse.failure("Credenciais inválidas");
    }
}
