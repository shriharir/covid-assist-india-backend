package com.help.covid.covidassistindiabackend.controller;

import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import com.help.covid.covidassistindiabackend.generic.GenericResponse;
import com.help.covid.covidassistindiabackend.model.Volunteer;
import com.help.covid.covidassistindiabackend.service.VolunteerService;
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
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/volunteer")
@RequiredArgsConstructor
@Slf4j
public class VolunteerController {

    private final ObjectMapper objectMapper;
    private final VolunteerService service;

    @PutMapping("/upsert")
    @SneakyThrows
    public ResponseEntity createPatientAssistRequest(@RequestBody @Valid Volunteer volunteer) {
        try {
            log.info("Received Volunteer upsert request with body {}::", objectMapper.writeValueAsString(volunteer));
            VolunteerEntity entity = service.upsert(volunteer);
            UUID volunteerId = entity.getVolunteerId();
            log.info("Created/Updated volunteer request with id {} ::", volunteerId);
            return ResponseEntity
                    .status(OK)
                    .body(GenericResponse.builder().id(volunteerId).build());
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/find/{volunteerId}")
    @SneakyThrows
    public ResponseEntity getVolunteer(@PathVariable UUID volunteerId) {
        try {
            log.info("Received Get volunteer details request for volunteerId {}::", volunteerId);
            VolunteerEntity entity = service.findByVolunteerId(volunteerId);
            log.info("Retrieved volunteer details with value {} ::", objectMapper.writeValueAsString(entity));
            return ResponseEntity
                    .status(OK)
                    .body(entity);
        } finally {
            MDC.clear();
        }
    }
}
