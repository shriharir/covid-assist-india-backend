package com.help.covid.covidassistindiabackend.mapper;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientAssistRequestMapper {
    PatientAssistRequestMapper INSTANCE = Mappers.getMapper(PatientAssistRequestMapper.class);

    PatientAssistRequestEntity toEntity(PatientAssistRequest order);

    PatientAssistRequest toDomain(PatientAssistRequestEntity order);

}
