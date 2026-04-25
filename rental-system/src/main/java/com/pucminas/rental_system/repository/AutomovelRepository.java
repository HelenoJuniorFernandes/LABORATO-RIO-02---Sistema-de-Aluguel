package com.pucminas.rental_system.repository;

import com.pucminas.rental_system.model.Automovel;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {}
