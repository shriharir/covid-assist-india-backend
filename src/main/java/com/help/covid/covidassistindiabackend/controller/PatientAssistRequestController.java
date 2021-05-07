package com.help.covid.covidassistindiabackend.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.covid.covidassistindiabackend.assembler.SearchTermsAssembler;
import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericResponse;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.service.PatientAssistRequestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.help.covid.covidassistindiabackend.util.ApplicationConstants.MAX_PAGE_LIMIT;
import static com.help.covid.covidassistindiabackend.util.ApplicationConstants.MIN_PAGE_NUMBER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@SuppressWarnings({"raw", "rawtypes", "unchecked"})
public class PatientAssistRequestController {

    private final ObjectMapper objectMapper;
    private final PatientAssistRequestService service;
    private final SearchTermsAssembler searchTermsAssembler;

    @PutMapping("/request")
    @SneakyThrows
    public ResponseEntity createPatientAssistRequest(@RequestBody @Valid PatientAssistRequest request) {
        try {
            log.info("Received Patient Assist request with body {}::", objectMapper.writeValueAsString(request));
            PatientAssistRequestEntity entity = service.create(request);
            UUID requestId = entity.getRequestId();
            log.info("Created patient assist request with id {} ::", requestId);
            return ResponseEntity
                    .status(OK)
                    .body(GenericResponse.builder().id(requestId).build());
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/request/{requestId}")
    @SneakyThrows
    public ResponseEntity getPatientAssistRequest(@PathVariable UUID requestId) {
        try {
            log.info("Received Get request for requestId {}::", requestId);
            PatientAssistRequestEntity entity = service.findByRequestId(requestId);
            log.info("Retrieved patient assist request with value {} ::", objectMapper.writeValueAsString(entity));
            return ResponseEntity
                    .status(OK)
                    .body(entity);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/request/findAll")
    @SneakyThrows
    public ResponseEntity getAllPatientAssistRequest(
            @RequestParam(value = "page", defaultValue = MIN_PAGE_NUMBER) Integer page,
            @RequestParam(value = "limit", defaultValue = MAX_PAGE_LIMIT) Integer limit,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "") List<String> status,
            @RequestParam(required = false, defaultValue = "") List<String> serviceTypes,
            @RequestParam(required = false) String volunteerId
    ) {
        try {
            log.info("Received request to get all requests");
            SearchTerms searchTerms = searchTermsAssembler.assemble(page, limit, fromDate, toDate, status, serviceTypes, volunteerId);
            Page<PatientAssistRequestEntity> entity = service.findAllRequests(searchTerms);
            log.info("Retrieved patient assist request with value {} ::", objectMapper.writeValueAsString(entity));
            return ResponseEntity
                    .status(OK)
                    .body(entity);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/assign/{requestId}")
    @SneakyThrows
    public ResponseEntity assignPatientAssistRequest(@PathVariable UUID requestId,
                                                     @RequestParam String volunteerId) {
        try {
            log.info("Received Assign request for request {} for volunteer {} ::", requestId, volunteerId);
            service.assignRequestToVolunteer(requestId, volunteerId);
            log.info("Assigned Request with id {} to volunteer id {}", requestId, volunteerId);
            return ResponseEntity
                    .status(OK)
                    .build();
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/unAssign/{requestId}")
    @SneakyThrows
    public ResponseEntity unAssignPatientAssistRequest(@PathVariable UUID requestId,
                                                       @RequestParam String volunteerId) {
        try {
            log.info("Received unAssign request for request {} for volunteer {} ::", requestId, volunteerId);
            service.unAssignRequestFromVolunteer(requestId, volunteerId);
            log.info("UnAssigned volunteer id {} from request with id {}", volunteerId, requestId);
            return ResponseEntity
                    .status(OK)
                    .build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        UUID requestId = UUID.randomUUID();
        return ResponseEntity
                .status(OK)
                .body("Hello world!!!");
    }
}
