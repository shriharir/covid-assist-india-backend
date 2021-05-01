package com.help.covid.covidassistindiabackend.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"raw", "rawtypes", "unchecked"})
public class PatientAssistRequestController {

    private final ObjectMapper objectMapper;
    private final PatientAssistRequestService service;

    @PostMapping("/request")
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

    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        UUID requestId = UUID.randomUUID();
        return ResponseEntity
                .status(OK)
                .body(GenericResponse.builder().id(requestId).build());
    }
}
