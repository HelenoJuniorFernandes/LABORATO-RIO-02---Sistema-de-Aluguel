package com.pucminas.rental_system.repository;

import com.pucminas.rental_system.model.Cliente;
import com.pucminas.rental_system.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    List<PedidoAluguel> findByCliente(Cliente cliente);
    List<PedidoAluguel> findByStatus(PedidoAluguel.PedidoStatus status);
    List<PedidoAluguel> findByClienteAndStatusIn(Cliente cliente, List<PedidoAluguel.PedidoStatus> statuses);
}
