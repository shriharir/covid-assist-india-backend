package com.help.covid.covidassistindiabackend.repository;

import java.util.Optional;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VolunteerRepository extends JpaRepository<VolunteerEntity, UUID> {
    Optional<VolunteerEntity> findByVolunteerId(String volunteerId);
}

