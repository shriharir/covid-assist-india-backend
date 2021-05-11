package com.help.covid.covidassistindiabackend.controller;

import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericResponse;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.service.LeadRequestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class LeadRequestController {

    private final ObjectMapper objectMapper;
    private final LeadRequestService service;

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
}
