package com.help.covid.covidassistindiabackend.repository;

import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientAssistRequestRepository extends JpaRepository<PatientAssistRequestEntity, UUID> {
}

