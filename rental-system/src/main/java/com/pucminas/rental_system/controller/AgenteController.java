package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.Agente;
import com.pucminas.rental_system.model.Cliente;
import com.pucminas.rental_system.model.PedidoAluguel;
import com.pucminas.rental_system.repository.AutomovelRepository;
import com.pucminas.rental_system.repository.ClienteRepository;
import com.pucminas.rental_system.repository.UserRepository;
import com.pucminas.rental_system.service.PedidoAluguelService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.transaction.Transactional;

@Transactional
@ExecuteOn(TaskExecutors.IO)
@Secured("ROLE_AGENTE")
@Controller("/agente")
public class AgenteController {

    @Inject private PedidoAluguelService pedidoService;
    @Inject private AutomovelRepository automovelRepository;
    @Inject private UserRepository userRepository;
    @Inject private ClienteRepository clienteRepository;
    
    @View("dashboard-agente")
    @Get("/dashboard")
    public Map<String, Object> dashboardAgente(Principal principal) {
        List<PedidoAluguel> todosPedidos = pedidoService.findAllPedidos();
        
        // Calcular estatísticas
        long totalPedidos = todosPedidos.size();
        long pedidosPendentes = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.PENDENTE)
            .count();
        long pedidosAprovados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.APROVADO)
            .count();
        long pedidosRejeitados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.REJEITADO)
            .count();
        
        // Pegar os 10 pedidos mais recentes
        List<PedidoAluguel> pedidosRecentes = todosPedidos.stream()
            .sorted((p1, p2) -> p2.getDataPedido().compareTo(p1.getDataPedido()))
            .limit(10)
            .toList();
        
        Map<String, Object> model = new HashMap<>();
        model.put("totalPedidos", totalPedidos);
        model.put("pedidosPendentes", pedidosPendentes);
        model.put("pedidosAprovados", pedidosAprovados);
        model.put("pedidosRejeitados", pedidosRejeitados);
        model.put("pedidosRecentes", pedidosRecentes);
        
        return model;
    }
    
    @View("agente-dados")
    @Get("/dados")
    public Map<String, Object> meusDados(Principal principal) {
        Agente agente = (Agente) userRepository.findByEmail(principal.getName());
        List<PedidoAluguel> todosPedidos = pedidoService.findAllPedidos();
        
        // Calcular estatísticas para a página de dados
        long totalPedidos = todosPedidos.size();
        long pedidosAprovados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.APROVADO)
            .count();
        long pedidosRejeitados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.REJEITADO)
            .count();
        long pedidosPendentes = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.PENDENTE)
            .count();
        
        Map<String, Object> model = new HashMap<>();
        model.put("agente", agente);
        model.put("totalPedidos", totalPedidos);
        model.put("pedidosAprovados", pedidosAprovados);
        model.put("pedidosRejeitados", pedidosRejeitados);
        model.put("pedidosPendentes", pedidosPendentes);
        
        return model;
    }
    
    @View("todos-pedidos")
    @Get("/todos-pedidos")
    public Map<String, Object> todosPedidos() {
        List<PedidoAluguel> todosPedidos = pedidoService.findAllPedidos();
        return Map.of("pedidos", todosPedidos);
    }
    
    @View("automoveis-cadastrados")
    @Get("/automoveis")
    public Map<String, Object> automoveisCadastrados() {
        return Map.of("automoveis", automovelRepository.findAll());
    }

    @View("cliente-details")
    @Get("/clientes/{id}")
    public Map<String, Object> showClienteDetails(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        double totalGastoAtualmente = pedidoService.calcularValorTotalPedidosAtivos(cliente);
        List<PedidoAluguel> pedidosAtivos = pedidoService.findPedidosAtivosPorCliente(cliente);

        Map<String, Object> model = new HashMap<>();
        model.put("cliente", cliente);
        model.put("totalGastoAtualmente", totalGastoAtualmente);
        model.put("pedidosAtivos", pedidosAtivos);
        return model;
    }
}
