package com.help.covid.covidassistindiabackend.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.covid.covidassistindiabackend.assembler.SearchTermsAssembler;
import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericResponse;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.service.LeadRequestService;
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
@RequestMapping("/leads")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class LeadRequestController {

    private final ObjectMapper objectMapper;
    private final LeadRequestService service;
    private final SearchTermsAssembler searchTermsAssembler;

    @PutMapping("/request")
    @SneakyThrows
    public ResponseEntity createLeadRequest(@RequestBody @Valid LeadRequest leadRequest) {
        try {
            log.info("Received Lead request with body {}::", objectMapper.writeValueAsString(leadRequest));
            LeadRequestEntity entity = service.create(leadRequest);
            UUID requestId = entity.getRequestId();
            log.info("Created lead request with id {} ::", requestId);
            return ResponseEntity
                    .status(OK)
                    .body(GenericResponse.builder().id(requestId).build());
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/request/{requestId}")
    @SneakyThrows
    public ResponseEntity getLeadRequest(@PathVariable UUID requestId) {
        try {
            log.info("Received Get lead request for requestId {}::", requestId);
            LeadRequestEntity entity = service.findByRequestId(requestId);
            log.info("Retrieved lead request with value {} ::", objectMapper.writeValueAsString(entity));
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
            @RequestParam(required = false, defaultValue = "") List<String> leadTypes,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String district
    ) {
        try {
            log.info("Received request to get all lead requests");
            SearchTerms searchTerms = searchTermsAssembler.assemble(page, limit, fromDate, toDate, status, leadTypes, null, state, district);
            Page<LeadRequestEntity> entity = service.findAllRequests(searchTerms);
            log.info("Retrieved lead requests with value {} ::", objectMapper.writeValueAsString(entity));
            return ResponseEntity
                    .status(OK)
                    .body(entity);
        } finally {
            MDC.clear();
        }
    }
}
