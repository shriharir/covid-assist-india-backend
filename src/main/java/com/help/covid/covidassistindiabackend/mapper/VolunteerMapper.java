package com.help.covid.covidassistindiabackend.mapper;

import com.help.covid.covidassistindiabackend.entity.VolunteerEntity;
import com.help.covid.covidassistindiabackend.model.Volunteer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VolunteerMapper {
    VolunteerMapper INSTANCE = Mappers.getMapper(VolunteerMapper.class);

    VolunteerEntity toEntity(Volunteer volunteer);

    Volunteer toDomain(VolunteerEntity volunteer);
}
