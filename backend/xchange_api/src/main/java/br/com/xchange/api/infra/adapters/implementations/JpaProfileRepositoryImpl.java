package br.com.xchange.api.infra.adapters.implementations;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xchange.api.infra.entities.ProfileJpa;

public interface JpaProfileRepositoryImpl extends JpaRepository<ProfileJpa, UUID> {
  
}
