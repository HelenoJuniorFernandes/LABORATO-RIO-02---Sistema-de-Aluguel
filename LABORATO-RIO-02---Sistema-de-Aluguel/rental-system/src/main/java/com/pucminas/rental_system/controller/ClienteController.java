package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.*;
import com.pucminas.rental_system.repository.*;
import com.pucminas.rental_system.service.PedidoAluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired private PedidoAluguelService pedidoService;
    @Autowired private AutomovelRepository automovelRepository;
    @Autowired private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboardCliente(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userRepository.findByEmail(userDetails.getUsername());
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
        
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("pedidosAtivos", pedidosAtivos);
        model.addAttribute("pedidosRejeitados", pedidosRejeitados);
        model.addAttribute("pedidosPendentes", pedidosPendentes);
        model.addAttribute("totalGastoAtualmente", totalGastoAtualmente);
        model.addAttribute("pedidosRecentes", pedidosRecentes);
        
        return "dashboard-cliente";
    }
    
    @GetMapping("/dados")
    public String meusDados(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userRepository.findByEmail(userDetails.getUsername());
        List<PedidoAluguel> todosPedidos = pedidoService.findPedidosPorCliente(cliente);
        
        // Calcular estatísticas para a página de dados
        long totalPedidos = todosPedidos.size();
        long pedidosAprovados = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.APROVADO)
            .count();
        long pedidosPendentes = todosPedidos.stream()
            .filter(p -> p.getStatus() == PedidoAluguel.PedidoStatus.PENDENTE)
            .count();
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("pedidosAprovados", pedidosAprovados);
        model.addAttribute("pedidosPendentes", pedidosPendentes);
        
        return "cliente-dados";
    }
    
    @GetMapping("/automoveis")
    public String automoveisDisponiveis(Model model) {
        model.addAttribute("automoveis", automovelRepository.findAll());
        return "automoveis-disponiveis";
    }
}
