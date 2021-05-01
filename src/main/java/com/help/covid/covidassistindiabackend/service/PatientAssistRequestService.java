package com.help.covid.covidassistindiabackend.service;

import java.util.List;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.repository.PatientAssistRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientAssistRequestService {

    @Autowired
    private PatientAssistRequestRepository repository;

    public PatientAssistRequestEntity create(PatientAssistRequest request) {
        return repository.save(request.toEntity());
    }

    public PatientAssistRequestEntity findByRequestId(UUID requestId) {
        return repository.findByRequestId(requestId)
                .orElseThrow(ResourceNotFoundException::requestNotFund);
    }

    public List<PatientAssistRequestEntity> findAllRequests() {
        return repository.findAll();
    }

}
