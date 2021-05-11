package com.help.covid.covidassistindiabackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRequestRepository extends JpaRepository<LeadRequestEntity, UUID> {
    Optional<LeadRequestEntity> findByRequestId(UUID requestId);

    List<LeadRequestEntity> findAll();
}

