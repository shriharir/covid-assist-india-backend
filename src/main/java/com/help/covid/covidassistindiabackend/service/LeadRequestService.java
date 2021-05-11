package com.help.covid.covidassistindiabackend.service;

import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.repository.LeadRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadRequestService {

    @Autowired
    private LeadRequestRepository repository;

    public LeadRequestEntity create(LeadRequest request) {
        UUID requestId = request.getRequestId();
        if (isEmpty(requestId)) {
            return repository.save(request.toEntity());
        } else {
            Optional<LeadRequestEntity> optional = repository.findByRequestId(requestId);
            if (optional.isPresent()) {
                LeadRequestEntity updatedEntity = request.toEntity();
                updatedEntity.setRequestId(requestId);
                return repository.save(updatedEntity);
            } else {
                return repository.save(request.toEntity());
            }
        }
    }

    public LeadRequestEntity findByRequestId(UUID requestId) {
        return repository.findByRequestId(requestId)
                .orElseThrow(ResourceNotFoundException::requestNotFund);
    }
}
