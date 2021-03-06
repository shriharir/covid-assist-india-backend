package com.help.covid.covidassistindiabackend.model;

import javax.validation.constraints.NotEmpty;

import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import com.help.covid.covidassistindiabackend.generic.GenericDomain;
import com.help.covid.covidassistindiabackend.mapper.VolunteerMapper;
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
public class Volunteer implements GenericDomain<VolunteerEntity> {
    @NotEmpty
    public String volunteerId;
    public String firstName;
    public String lastName;
    public String email;
    public String primaryMobile;
    public String alternateMobile;
    public String age;
    public Address address;
    public String idProofNumber;

    @Override
    public VolunteerEntity toEntity() {
        return VolunteerMapper.INSTANCE.toEntity(this);

    }
}
