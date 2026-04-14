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
@RequestMapping("/pedidos")
public class PedidoAluguelController {

    @Autowired private PedidoAluguelService pedidoService;
    @Autowired private AutomovelRepository automovelRepository;
    @Autowired private UserRepository userRepository;
    
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if ("ROLE_AGENTE".equals(user.getRole())) {
            return "redirect:/agente/dashboard";
        }
        return "redirect:/cliente/dashboard";
    }

    @GetMapping("/mine")
    public String getMeusPedidos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userRepository.findByEmail(userDetails.getUsername());
        List<PedidoAluguel> pedidos = pedidoService.findPedidosPorCliente(cliente);
        model.addAttribute("pedidos", pedidos);
        return "meus-pedidos";
    }
    
    @GetMapping("/pending")
    public String getPedidosPendentes(Model model) {
        List<PedidoAluguel> pedidos = pedidoService.findPedidosPendentes();
        model.addAttribute("pedidos", pedidos);
        return "pedidos-pendentes";
    }

    @GetMapping("/new")
    public String showPedidoForm(Model model) {
        model.addAttribute("automoveis", automovelRepository.findAll());
        return "pedido-form";
    }

    @PostMapping("/new")
    public String createPedido(@RequestParam Long automovelId,
                               @RequestParam String dataRetirada,
                               @RequestParam String dataDevolucao,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = (Cliente) userRepository.findByEmail(userDetails.getUsername());
        pedidoService.criarPedido(cliente.getId(), automovelId, dataRetirada, dataDevolucao);
        return "redirect:/pedidos/mine";
    }
    
    @PostMapping("/evaluate")
    public String evaluatePedido(@RequestParam Long pedidoId, @RequestParam boolean aprovar, @AuthenticationPrincipal UserDetails userDetails) {
        Agente agente = (Agente) userRepository.findByEmail(userDetails.getUsername());
        pedidoService.avaliarPedido(pedidoId, agente.getId(), aprovar);
        return "redirect:/pedidos/pending";
    }
}
