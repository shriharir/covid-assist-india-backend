package com.help.covid.covidassistindiabackend.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.generic.GenericDomain;
import com.help.covid.covidassistindiabackend.mapper.LeadRequestMapper;
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
public class LeadRequest implements GenericDomain<LeadRequestEntity> {
    private UUID requestId;
    private String leadType;
    private String leadDescription;
    private String verifiedStatus;
    private Boolean isVerified;
    private String lastVerifiedBy;
    private ZonedDateTime lastVerifiedAt;
    private ZonedDateTime createdAt;
    private List<VolunteerComment> comments;
    private String imageUrl;
    private String businessName;
    private Address businessAddress;
    private String contactPerson;
    public String primaryMobile;
    public String secondaryMobile;
    private Boolean stockAvailable;
    private String informationSource;

    @Override
    public LeadRequestEntity toEntity() {
        return LeadRequestMapper.INSTANCE.toEntity(this);
    }
}
