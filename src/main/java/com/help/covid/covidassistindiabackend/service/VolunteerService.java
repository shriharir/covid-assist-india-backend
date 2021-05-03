package com.help.covid.covidassistindiabackend.service;

import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import com.help.covid.covidassistindiabackend.exception.ResourceNotFoundException;
import com.help.covid.covidassistindiabackend.model.Volunteer;
import com.help.covid.covidassistindiabackend.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolunteerService {

    @Autowired
    private VolunteerRepository repository;

    public VolunteerEntity upsert(Volunteer volunteer) {
        UUID volunteerId = volunteer.getVolunteerId();
        if (isEmpty(volunteerId)) {
            return repository.save(volunteer.toEntity());
        } else {
            Optional<VolunteerEntity> optional = repository.findByVolunteerId(volunteerId);
            if (optional.isPresent()) {
                VolunteerEntity existingEntity = optional.get();
                VolunteerEntity updatedEntity = volunteer.toEntity();
                updatedEntity.setEmail(existingEntity.getEmail());
                updatedEntity.setPrimaryMobile(existingEntity.getPrimaryMobile());
                updatedEntity.setVolunteerId(volunteerId);
                return repository.save(updatedEntity);
            } else {
                return repository.save(volunteer.toEntity());
            }
        }
    }

    public VolunteerEntity findByVolunteerId(UUID volunteerId) {
        return repository.findByVolunteerId(volunteerId)
                .orElseThrow(ResourceNotFoundException::requestNotFund);
    }

}
