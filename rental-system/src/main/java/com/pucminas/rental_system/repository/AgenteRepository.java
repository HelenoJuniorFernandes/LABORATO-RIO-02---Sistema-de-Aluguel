package com.pucminas.rental_system.repository;

import com.pucminas.rental_system.model.Agente;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {}
