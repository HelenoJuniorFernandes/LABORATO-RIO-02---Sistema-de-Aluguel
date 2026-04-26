package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.Automovel;
import com.pucminas.rental_system.repository.AutomovelRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.Map;

@Transactional
@ExecuteOn(TaskExecutors.IO)
@Secured("ROLE_AGENTE")
@Controller("/automoveis")
public class AutomovelController {

    @Inject
    private AutomovelRepository automovelRepository;

    @View("automovel-form")
    @Get("/novo")
    public Map<String, Object> showNovoForm() {
        return Map.of("automovel", new Automovel());
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/novo")
    public HttpResponse<?> criarAutomovel(@Body Automovel automovel) {
        try {
            automovelRepository.save(automovel);
            return HttpResponse.seeOther(URI.create("/agente/automoveis"));
        } catch (Exception e) {
            return HttpResponse.seeOther(URI.create("/automoveis/novo"));
        }
    }

    @Get("/editar/{id}")
    public HttpResponse<?> showEditarForm(@PathVariable Long id) {
        try {
            Automovel automovel = automovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            return HttpResponse.ok(new ModelAndView<>("automovel-edit-form", Map.of("automovel", automovel)));
        } catch (Exception e) {
            return HttpResponse.seeOther(URI.create("/agente/automoveis"));
        }
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/editar/{id}")
    public HttpResponse<?> editarAutomovel(@PathVariable Long id, 
                                           @Body Automovel automovelAtualizado) {
        try {
            Automovel automovel = automovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            
            automovel.setMatricula(automovelAtualizado.getMatricula());
            automovel.setAno(automovelAtualizado.getAno());
            automovel.setMarca(automovelAtualizado.getMarca());
            automovel.setModelo(automovelAtualizado.getModelo());
            automovel.setPlaca(automovelAtualizado.getPlaca());
            automovel.setCor(automovelAtualizado.getCor());
            automovel.setValorAluguelDiario(automovelAtualizado.getValorAluguelDiario());
            automovel.setImagemUrl(automovelAtualizado.getImagemUrl());
            
            automovelRepository.save(automovel);
            return HttpResponse.seeOther(URI.create("/agente/automoveis"));
        } catch (Exception e) {
            return HttpResponse.seeOther(URI.create("/automoveis/editar/" + id));
        }
    }

}
