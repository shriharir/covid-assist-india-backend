package com.help.covid.covidassistindiabackend.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
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
import com.help.covid.covidassistindiabackend.mapper.LeadRequestMapper;
import com.help.covid.covidassistindiabackend.model.Address;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.model.VolunteerComment;
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
import static java.util.UUID.randomUUID;

@Getter
@Setter
@Entity
@Table(name = "lead_requests")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "JsonType", typeClass = JsonType.class),
        @TypeDef(name = "StringArray", typeClass = StringArrayType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Slf4j
public class LeadRequestEntity implements GenericEntity<LeadRequest>, Serializable {
    @Id
    @Column(name = "id")
    private UUID requestId;

    private String leadType;
    private String leadDescription;
    private String verifiedStatus;
    private Boolean isVerified;
    private String lastVerifiedBy;
    private ZonedDateTime lastVerifiedAt;
    private ZonedDateTime createdAt;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<VolunteerComment> comments;

    private String imageUrl;
    private String businessName;

    @Type(type = JSON_TYPE)
    private Address businessAddress;

    private String contactPerson;
    public String primaryMobile;
    public String secondaryMobile;
    private Boolean stockAvailable;
    private String informationSource;

    @Override
    public LeadRequest toDomain() {
        return LeadRequestMapper.INSTANCE.toDomain(this);
    }

    @SneakyThrows
    @PrePersist
    public void updateRequiredFields() {
        log.info("In Pre Persist Method for request");
        if (this.requestId == null) {
            this.requestId = randomUUID();
        }
        createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        log.info("In Pre Update Method");
    }
}
