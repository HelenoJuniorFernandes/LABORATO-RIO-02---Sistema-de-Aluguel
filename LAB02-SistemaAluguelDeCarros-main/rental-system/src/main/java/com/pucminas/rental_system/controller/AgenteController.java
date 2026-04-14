package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.Agente;
import com.pucminas.rental_system.model.Cliente;
import com.pucminas.rental_system.model.PedidoAluguel;
import com.pucminas.rental_system.repository.AutomovelRepository;
import com.pucminas.rental_system.repository.ClienteRepository;
import com.pucminas.rental_system.repository.UserRepository;
import com.pucminas.rental_system.service.PedidoAluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/agente")
public class AgenteController {

    @Autowired private PedidoAluguelService pedidoService;
    @Autowired private AutomovelRepository automovelRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ClienteRepository clienteRepository;
    
    @GetMapping("/dashboard")
    public String dashboardAgente(Model model, @AuthenticationPrincipal UserDetails userDetails) {
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
        
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("pedidosPendentes", pedidosPendentes);
        model.addAttribute("pedidosAprovados", pedidosAprovados);
        model.addAttribute("pedidosRejeitados", pedidosRejeitados);
        model.addAttribute("pedidosRecentes", pedidosRecentes);
        
        return "dashboard-agente";
    }
    
    @GetMapping("/dados")
    public String meusDados(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Agente agente = (Agente) userRepository.findByEmail(userDetails.getUsername());
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
        
        model.addAttribute("agente", agente);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("pedidosAprovados", pedidosAprovados);
        model.addAttribute("pedidosRejeitados", pedidosRejeitados);
        model.addAttribute("pedidosPendentes", pedidosPendentes);
        
        return "agente-dados";
    }
    
    @GetMapping("/todos-pedidos")
    public String todosPedidos(Model model) {
        List<PedidoAluguel> todosPedidos = pedidoService.findAllPedidos();
        model.addAttribute("pedidos", todosPedidos);
        return "todos-pedidos";
    }
    
    @GetMapping("/automoveis")
    public String automoveisCadastrados(Model model) {
        model.addAttribute("automoveis", automovelRepository.findAll());
        return "automoveis-cadastrados";
    }

    @GetMapping("/clientes/{id}")
    public String showClienteDetails(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        double totalGastoAtualmente = pedidoService.calcularValorTotalPedidosAtivos(cliente);
        List<PedidoAluguel> pedidosAtivos = pedidoService.findPedidosAtivosPorCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("totalGastoAtualmente", totalGastoAtualmente);
        model.addAttribute("pedidosAtivos", pedidosAtivos);
        return "cliente-details";
    }
}
