package com.help.covid.covidassistindiabackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientAssistRequestRepository extends JpaRepository<PatientAssistRequestEntity, UUID> {
    Optional<PatientAssistRequestEntity> findByRequestId(UUID requestId);

    List<PatientAssistRequestEntity> findAll();
}

