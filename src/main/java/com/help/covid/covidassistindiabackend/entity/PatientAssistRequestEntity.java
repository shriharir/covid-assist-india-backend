package com.help.covid.covidassistindiabackend.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.help.covid.covidassistindiabackend.generic.GenericEntity;
import com.help.covid.covidassistindiabackend.generic.JsonType;
import com.help.covid.covidassistindiabackend.generic.StringArrayType;
import com.help.covid.covidassistindiabackend.mapper.PatientAssistRequestMapper;
import com.help.covid.covidassistindiabackend.model.Address;
import com.help.covid.covidassistindiabackend.model.CareTakerDetails;
import com.help.covid.covidassistindiabackend.model.HospitalDetails;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.model.PatientDetails;
import com.help.covid.covidassistindiabackend.model.RequestStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import static com.help.covid.covidassistindiabackend.generic.JsonType.JSON_TYPE;
import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.comparing;
import static java.util.UUID.randomUUID;
import static org.springframework.util.CollectionUtils.isEmpty;

@Getter
@Setter
@Entity
@Table(name = "patient_assist_requests")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "JsonType", typeClass = JsonType.class),
        @TypeDef(name = "StringArray", typeClass = StringArrayType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Slf4j
public class PatientAssistRequestEntity implements GenericEntity<PatientAssistRequest>, Serializable {

    @Id
    @Column(name = "id")
    private UUID requestId;

    private String srfId;
    private String buNumber;
    public String covidTestResult;
    public String isVaccinationTaken;

    @Type(type = JSON_TYPE)
    public PatientDetails patientDetails;

    @Type(type = JSON_TYPE)
    public CareTakerDetails careTakerDetails;

    @Type(type = JSON_TYPE)
    public Address address;

    @Type(type = JSON_TYPE)
    public HospitalDetails hospitalDetails;
    public String serviceRequested;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    public List<String> description;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    public List<RequestStatus> requestStatus;

    public String currentStatus;

    public String volunteerId;

    public ZonedDateTime createdAt;
    public ZonedDateTime lastModifiedAt;

    @Override
    public PatientAssistRequest toDomain() {
        return PatientAssistRequestMapper.INSTANCE.toDomain(this);
    }

    @SneakyThrows
    @PrePersist
    public void updateRequiredFields() {
        log.info("In Pre Persist Method for request");
        if (this.requestId == null) {
            this.requestId = randomUUID();
        }
        createdAt = ZonedDateTime.now();

        if (requestStatus == null) {
            requestStatus = new ArrayList<>();
        }

        if (isEmpty(requestStatus)) {
            requestStatus.add(RequestStatus
                    .builder()
                    .status("OPEN")
                    .eventTime(ZonedDateTime.now(UTC))
                    .comments("Initial Status Created")
                    .build());
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        log.info("In Pre Update Method");
        lastModifiedAt = ZonedDateTime.now();

        findCurrentStatus().ifPresentOrElse(status -> {
                    this.currentStatus = status.getStatus();
                    log.info("Updated the current status to {} for request :: {}", currentStatus, requestId);
                },
                () -> this.currentStatus = "");
    }

    private Optional<RequestStatus> findCurrentStatus() {
        return isEmpty(requestStatus) ? Optional.empty() : requestStatus.stream()
                .sorted(comparing(RequestStatus::getEventTime).reversed())
                .findFirst();
    }

}
