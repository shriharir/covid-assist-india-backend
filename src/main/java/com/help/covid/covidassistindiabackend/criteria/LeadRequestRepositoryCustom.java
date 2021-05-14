package com.help.covid.covidassistindiabackend.criteria;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import org.springframework.data.domain.Page;

public interface LeadRequestRepositoryCustom {
    Page<LeadRequestEntity> searchByRequestFilters(SearchTerms searchTerms);
}
