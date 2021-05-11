package com.help.covid.covidassistindiabackend.mapper;

import java.util.List;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.model.LeadRequest;
import com.help.covid.covidassistindiabackend.model.PatientAssistRequest;
import com.help.covid.covidassistindiabackend.model.VolunteerComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeadRequestMapper {
    LeadRequestMapper INSTANCE = Mappers.getMapper(LeadRequestMapper.class);
    String COMMENTS = "comments";
    String MAP_COMMENTS = "mapComments";

    @Mappings({
            @Mapping(source = COMMENTS, target = COMMENTS, qualifiedByName = MAP_COMMENTS)
    })
    LeadRequestEntity toEntity(LeadRequest leadRequest);

    LeadRequest toDomain(LeadRequestEntity leadRequestEntity);

    @Named(MAP_COMMENTS)
    static List<VolunteerComment> mapTotals(List<VolunteerComment> comments) {
        return comments;
    }

}
