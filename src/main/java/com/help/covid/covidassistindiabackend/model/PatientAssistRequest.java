package com.help.covid.covidassistindiabackend.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericDomain;
import com.help.covid.covidassistindiabackend.mapper.PatientAssistRequestMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PatientAssistRequest implements GenericDomain<PatientAssistRequestEntity> {
    private UUID requestId;
    private String srfId;
    private String buNumber;
    public String covidTestResult;
    public String isVaccinationTaken;
    public PatientDetails patientDetails;
    public CareTakerDetails careTakerDetails;
    public Address address;
    public HospitalDetails hospitalDetails;
    public String serviceRequested;
    public List<String> description;
    public List<RequestStatus> requestStatus;
    public List<VolunteerComment> comments;
    public String volunteerId;
    public ZonedDateTime createdAt;
    public ZonedDateTime lastModifiedAt;

    @Override
    public PatientAssistRequestEntity toEntity() {
        return PatientAssistRequestMapper.INSTANCE.toEntity(this);
    }
}
