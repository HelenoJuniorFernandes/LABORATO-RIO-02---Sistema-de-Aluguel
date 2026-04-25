package com.pucminas.rental_system.repository;

import com.pucminas.rental_system.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {}
