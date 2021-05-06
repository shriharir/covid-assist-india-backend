package com.help.covid.covidassistindiabackend.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchTerms {
    private Integer pageNumber;
    private Integer limit;
    private DateRange dateRange;
    private List<String> statuses;
    private List<String> serviceTypes;
    private String volunteerId;
}
