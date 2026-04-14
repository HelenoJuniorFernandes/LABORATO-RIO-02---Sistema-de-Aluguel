package com.pucminas.rental_system.service;

import com.pucminas.rental_system.model.*;
import com.pucminas.rental_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PedidoAluguelService {
    @Autowired private PedidoAluguelRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AutomovelRepository automovelRepository;
    @Autowired private AgenteRepository agenteRepository;

    public PedidoAluguel criarPedido(Long clienteId, Long automovelId, String dataRetiradaStr, String dataDevolucaoStr) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Automovel automovel = automovelRepository.findById(automovelId).orElseThrow(() -> new RuntimeException("Automóvel não encontrado"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataRetirada = LocalDateTime.parse(dataRetiradaStr, formatter);
        LocalDateTime dataDevolucao = LocalDateTime.parse(dataDevolucaoStr, formatter);

        if (dataRetirada.isAfter(dataDevolucao)) {
            throw new RuntimeException("A data de retirada não pode ser depois da data de devolução.");
        }

        long dias = ChronoUnit.DAYS.between(dataRetirada.toLocalDate(), dataDevolucao.toLocalDate());
        double valorTotal = automovel.getValorAluguelDiario() * (dias == 0 ? 1 : dias);

        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataRetirada(dataRetirada);
        pedido.setDataDevolucao(dataDevolucao);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(PedidoAluguel.PedidoStatus.PENDENTE);
        
        return pedidoRepository.save(pedido);
    }
    
    public PedidoAluguel avaliarPedido(Long pedidoId, Long agenteId, boolean aprovar) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Agente agente = agenteRepository.findById(agenteId).orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        pedido.setAgente(agente);
        pedido.setStatus(aprovar ? PedidoAluguel.PedidoStatus.APROVADO : PedidoAluguel.PedidoStatus.REJEITADO);
        
        return pedidoRepository.save(pedido);
    }
    
    public List<PedidoAluguel> findPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
    
    public List<PedidoAluguel> findPedidosPendentes() {
        return pedidoRepository.findByStatus(PedidoAluguel.PedidoStatus.PENDENTE);
    }
    
    public List<PedidoAluguel> findAllPedidos() {
        return pedidoRepository.findAll();
    }

    public double calcularValorTotalPedidosAtivos(Cliente cliente) {
        List<PedidoAluguel> pedidosAtivos = pedidoRepository.findByClienteAndStatusIn(
            cliente, 
            List.of(PedidoAluguel.PedidoStatus.PENDENTE, PedidoAluguel.PedidoStatus.APROVADO)
        );
        return pedidosAtivos.stream().mapToDouble(PedidoAluguel::getValorTotal).sum();
    }

    public List<PedidoAluguel> findPedidosAtivosPorCliente(Cliente cliente) {
        return pedidoRepository.findByClienteAndStatusIn(
            cliente, 
            List.of(PedidoAluguel.PedidoStatus.PENDENTE, PedidoAluguel.PedidoStatus.APROVADO)
        );
    }
}
