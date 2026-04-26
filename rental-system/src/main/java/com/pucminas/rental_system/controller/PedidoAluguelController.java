package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.*;
import com.pucminas.rental_system.repository.*;
import com.pucminas.rental_system.service.PedidoAluguelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Transactional
@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/pedidos")
public class PedidoAluguelController {

    @Inject private PedidoAluguelService pedidoService;
    @Inject private AutomovelRepository automovelRepository;
    @Inject private UserRepository userRepository;
    
    @Get
    public HttpResponse<?> index(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if ("ROLE_AGENTE".equals(user.getRole())) {
            return HttpResponse.seeOther(URI.create("/agente/dashboard"));
        }
        return HttpResponse.seeOther(URI.create("/cliente/dashboard"));
    }

    @View("meus-pedidos")
    @Get("/mine")
    public Map<String, Object> getMeusPedidos(Principal principal) {
        Cliente cliente = (Cliente) userRepository.findByEmail(principal.getName());
        List<PedidoAluguel> pedidos = pedidoService.findPedidosPorCliente(cliente);
        return Map.of("pedidos", pedidos);
    }
    
    @View("pedidos-pendentes")
    @Get("/pending")
    public Map<String, Object> getPedidosPendentes() {
        List<PedidoAluguel> pedidos = pedidoService.findPedidosPendentes();
        return Map.of("pedidos", pedidos);
    }

    @View("pedido-form")
    @Get("/new")
    public Map<String, Object> showPedidoForm() {
        return Map.of("automoveis", automovelRepository.findAll());
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/new")
    public HttpResponse<?> createPedido(Long automovelId,
                                        String dataRetirada,
                                        String dataDevolucao,
                                        Principal principal) {
        Cliente cliente = (Cliente) userRepository.findByEmail(principal.getName());
        pedidoService.criarPedido(cliente.getId(), automovelId, dataRetirada, dataDevolucao);
        return HttpResponse.seeOther(URI.create("/pedidos/mine"));
    }
    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/evaluate")
    public HttpResponse<?> evaluatePedido(Long pedidoId, boolean aprovar, Principal principal) {
        Agente agente = (Agente) userRepository.findByEmail(principal.getName());
        pedidoService.avaliarPedido(pedidoId, agente.getId(), aprovar);
        return HttpResponse.seeOther(URI.create("/pedidos/pending"));
    }
}
