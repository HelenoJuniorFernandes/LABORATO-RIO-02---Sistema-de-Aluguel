package com.pucminas.rental_system.service;

import com.pucminas.rental_system.model.User;
import com.pucminas.rental_system.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.transaction.Transactional;

import java.util.Collections;

@ExecuteOn(TaskExecutors.IO)
@Singleton
public class UserDetailsServiceImpl<B> implements HttpRequestAuthenticationProvider<B> {

    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public AuthenticationResponse authenticate(HttpRequest<B> httpRequest,
                                               AuthenticationRequest<String, String> authenticationRequest) {
        String email = authenticationRequest.getIdentity();
        String password = authenticationRequest.getSecret();
        
        User user = userRepository.findByEmail(email);
        
        boolean passwordMatches = false;
        if (user != null) {
            try {
                // Tenta validar usando a criptografia BCrypt
                passwordMatches = BCrypt.checkpw(password, user.getPassword());
            } catch (IllegalArgumentException e) {
                // Fallback: Se der erro (a senha no banco não for hash), tenta comparar como texto puro
                passwordMatches = user.getPassword().equals(password);
            }
        }
        
        if (passwordMatches) {
            return AuthenticationResponse.success(
                user.getEmail(), 
                Collections.singleton(user.getRole())
            );
        }
        
        return AuthenticationResponse.failure("Credenciais inválidas");
    }
}
