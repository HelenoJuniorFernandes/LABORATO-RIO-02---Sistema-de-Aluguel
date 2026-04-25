package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.*;
import com.pucminas.rental_system.repository.*;
import com.pucminas.rental_system.service.PedidoAluguelService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Secured("ROLE_CLIENTE")
@Controller("/cliente")
public class ClienteController {

    @Inject private PedidoAluguelService pedidoService;
    @Inject private AutomovelRepository automovelRepository;
    @Inject private UserRepository userRepository;
    
    @View("dashboard-cliente")
    @Get("/dashboard")
    public Map<String, Object> dashboardCliente(Principal principal) {
        Cliente cliente = (Cliente) userRepository.findByEmail(principal.getName());
        List<PedidoAluguel> todosPedidos = pedidoService.findPedidosPorCliente(cliente);
        
        // Calcular estatísticas
        long totalPedidos = todosPedidos.size();
        long pedidosAtivos = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.APROVADO)
            .count();
        long pedidosRejeitados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.REJEITADO)
            .count();
        long pedidosPendentes = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.PENDENTE)
            .count();
        
        // Calcular valor total de pedidos ativos
        double totalGastoAtualmente = pedidoService.calcularValorTotalPedidosAtivos(cliente);

        // Pegar os 5 pedidos mais recentes
        List<PedidoAluguel> pedidosRecentes = todosPedidos.stream()
            .sorted((p1, p2) -> p2.getDataPedido().compareTo(p1.getDataPedido()))
            .limit(5)
            .toList();
        
        Map<String, Object> model = new HashMap<>();
        model.put("totalPedidos", totalPedidos);
        model.put("pedidosAtivos", pedidosAtivos);
        model.put("pedidosRejeitados", pedidosRejeitados);
        model.put("pedidosPendentes", pedidosPendentes);
        model.put("totalGastoAtualmente", totalGastoAtualmente);
        model.put("pedidosRecentes", pedidosRecentes);
        
        return model;
    }
    
    @View("cliente-dados")
    @Get("/dados")
    public Map<String, Object> meusDados(Principal principal) {
        Cliente cliente = (Cliente) userRepository.findByEmail(principal.getName());
        List<PedidoAluguel> todosPedidos = pedidoService.findPedidosPorCliente(cliente);
        
        // Calcular estatísticas para a página de dados
        long totalPedidos = todosPedidos.size();
        long pedidosAprovados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.APROVADO)
            .count();
        long pedidosPendentes = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.PENDENTE)
            .count();
        
        Map<String, Object> model = new HashMap<>();
        model.put("cliente", cliente);
        model.put("totalPedidos", totalPedidos);
        model.put("pedidosAprovados", pedidosAprovados);
        model.put("pedidosPendentes", pedidosPendentes);
        
        return model;
    }
    
    @View("automoveis-disponiveis")
    @Get("/automoveis")
    public Map<String, Object> automoveisDisponiveis() {
        return Map.of("automoveis", automovelRepository.findAll());
    }
}
