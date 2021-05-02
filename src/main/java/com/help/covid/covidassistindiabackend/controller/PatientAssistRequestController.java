package com.help.covid.covidassistindiabackend.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericResponse;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.service.PatientAssistRequestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"raw", "rawtypes", "unchecked"})
public class PatientAssistRequestController {

    private final ObjectMapper objectMapper;
    private final PatientAssistRequestService service;

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
    public ResponseEntity getAllPatientAssistRequest() {
        try {
            log.info("Received request to get all requests");
            List<PatientAssistRequestEntity> entity = service.findAllRequests();
            log.info("Retrieved patient assist request with value {} ::", objectMapper.writeValueAsString(entity));
            return ResponseEntity
                    .status(OK)
                    .body(entity);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        UUID requestId = UUID.randomUUID();
        return ResponseEntity
                .status(OK)
                .body(GenericResponse.builder().id(requestId).build());
    }
}
