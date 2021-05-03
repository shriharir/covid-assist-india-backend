package com.help.covid.covidassistindiabackend.service;

import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.criteria.PatientAssistRequestRepositoryCustom;
import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.repository.PatientAssistRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientAssistRequestService {

    @Autowired
    private PatientAssistRequestRepository repository;

    @Autowired
    private PatientAssistRequestRepositoryCustom repositoryCustom;

    public PatientAssistRequestEntity create(PatientAssistRequest request) {
        UUID requestId = request.getRequestId();
        if (isEmpty(requestId)) {
            return repository.save(request.toEntity());
        } else {
            Optional<PatientAssistRequestEntity> optional = repository.findByRequestId(requestId);
            if (optional.isPresent()) {
                PatientAssistRequestEntity updatedEntity = request.toEntity();
                updatedEntity.setRequestId(requestId);
                return repository.save(updatedEntity);
            } else {
                return repository.save(request.toEntity());
            }
        }
    }

    public PatientAssistRequestEntity findByRequestId(UUID requestId) {
        return repository.findByRequestId(requestId)
                .orElseThrow(ResourceNotFoundException::requestNotFund);
    }

    public Page<PatientAssistRequestEntity> findAllRequests(SearchTerms searchTerms) {
        return repositoryCustom.searchByRequestFilters(searchTerms);
    }

}
