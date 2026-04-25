package com.pucminas.rental_system.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import io.micronaut.core.annotation.Nullable;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class AuthController {
    
    @View("login")
    @Get("/login")
    public HttpResponse<?> login(@Nullable Principal principal, 
                                 @Nullable @QueryValue Boolean error, 
                                 @Nullable @QueryValue Boolean logout) {
        if (principal != null) {
            return HttpResponse.redirect(URI.create("/pedidos"));
        }
        return HttpResponse.ok(Map.of(
            "error", error != null && error,
            "logout", logout != null && logout
        ));
    }

    @Get("/")
    public HttpResponse<?> home(@Nullable Principal principal) {
        if (principal == null) {
            return HttpResponse.redirect(URI.create("/login"));
        }
        return HttpResponse.redirect(URI.create("/pedidos"));
    }
}
