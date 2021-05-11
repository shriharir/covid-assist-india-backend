package com.help.covid.covidassistindiabackend.service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.criteria.PatientAssistRequestRepositoryCustom;
import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import com.help.covid.covidassistindiabackend.exception.BadRequest;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.model.RequestStatus;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.model.VolunteerComment;
import com.help.covid.covidassistindiabackend.repository.PatientAssistRequestRepository;
import com.help.covid.covidassistindiabackend.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static java.time.ZoneOffset.UTC;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientAssistRequestService {

    @Autowired
    private PatientAssistRequestRepository repository;

    @Autowired
    private PatientAssistRequestRepositoryCustom repositoryCustom;

    @Autowired
    private VolunteerRepository volunteerRepository;

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

    public void assignRequestToVolunteer(UUID requestId, String volunteerId) {
        Optional<PatientAssistRequestEntity> optionalRequest = repository.findByRequestId(requestId);
        optionalRequest.ifPresentOrElse(requestEntity -> {
            Optional<VolunteerEntity> volunteerEntity = volunteerRepository.findByVolunteerId(volunteerId);
            volunteerEntity.ifPresentOrElse(entity -> {
                requestEntity.setVolunteerId(volunteerId);
                requestEntity.requestStatus.add(RequestStatus
                        .builder()
                        .status("ASSIGNED")
                        .eventTime(ZonedDateTime.now(UTC))
                        .volunteerId(volunteerId)
                        .updatedBy(volunteerId)
                        .comments("Request Assigned to volunteer ::" + volunteerId)
                        .build());
                repository.save(requestEntity);
            }, () -> {
                throw ResourceNotFoundException.volunteerNotFund();
            });

        }, () -> {
            throw ResourceNotFoundException.requestNotFund();
        });
    }

    public void unAssignRequestFromVolunteer(UUID requestId, String volunteerId) {
        Optional<PatientAssistRequestEntity> optionalRequest = repository.findByRequestId(requestId);
        optionalRequest.ifPresentOrElse(requestEntity -> {
            if (requestEntity.getVolunteerId().equalsIgnoreCase(volunteerId)) {
                requestEntity.setVolunteerId("");
                requestEntity.requestStatus.add(RequestStatus
                        .builder()
                        .status("UNASSIGNED")
                        .eventTime(ZonedDateTime.now(UTC))
                        .volunteerId(volunteerId)
                        .updatedBy(volunteerId)
                        .comments("Request UN Assigned from volunteer ::" + volunteerId)
                        .build());
                repository.save(requestEntity);
            } else {
                throw BadRequest.volunteerNotAssignedToRequest();
            }
        }, () -> {
            throw ResourceNotFoundException.requestNotFund();
        });
    }

    public void addCommentToRequest(UUID requestId, VolunteerComment comment) {
        Optional<PatientAssistRequestEntity> optionalRequest = repository.findByRequestId(requestId);
        optionalRequest.ifPresentOrElse(requestEntity -> {
            if (comment.getEventTime() == null) {
                comment.eventTime = ZonedDateTime.now();
            }
            requestEntity.getComments().add(comment);
            repository.save(requestEntity);
        }, () -> {
            throw ResourceNotFoundException.requestNotFund();
        });

    }

}
