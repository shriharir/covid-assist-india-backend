package com.help.covid.covidassistindiabackend.service;

import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.criteria.LeadRequestRepositoryCustom;
import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.exception.BadRequest;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.repository.LeadRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadRequestService {

    @Autowired
    private LeadRequestRepository repository;

    @Autowired
    private LeadRequestRepositoryCustom repositoryCustom;

    public LeadRequestEntity create(LeadRequest request) {
        UUID requestId = request.getRequestId();
        long duplicateCount = repository.countByBusinessNameOrContactPerson(request.getBusinessName(),
                request.getContactPerson());

        if (duplicateCount == 0 || (duplicateCount == 1 && request.getRequestId() != null)) {
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
        } else {
            throw BadRequest.leadRequestAlreadyExists();
        }
    }

    public LeadRequestEntity findByRequestId(UUID requestId) {
        return repository.findByRequestId(requestId)
                .orElseThrow(ResourceNotFoundException::requestNotFund);
    }

    public Page<LeadRequestEntity> findAllRequests(SearchTerms searchTerms) {
        return repositoryCustom.searchByRequestFilters(searchTerms);
    }
}
