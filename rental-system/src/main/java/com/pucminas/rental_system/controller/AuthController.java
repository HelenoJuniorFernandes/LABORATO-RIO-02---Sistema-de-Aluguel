package com.pucminas.rental_system.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import java.net.URI;

@Controller
public class AuthController {
    
    @View("login")
    @Get("/login")
    public HttpResponse<?> login() {
        return HttpResponse.ok();
    }

    @Get("/")
    public HttpResponse<?> home() {
        return HttpResponse.redirect(URI.create("/pedidos"));
    }
}
