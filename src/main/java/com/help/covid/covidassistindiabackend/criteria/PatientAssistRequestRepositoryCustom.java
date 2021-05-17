package com.help.covid.covidassistindiabackend.criteria;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import org.springframework.data.domain.Page;

public interface PatientAssistRequestRepositoryCustom {
    Page<PatientAssistRequestEntity> searchByRequestFilters(SearchTerms searchTerms);

    Long findDuplicateRequestCount(String srfId, String buNumber, String primaryCareTakerPhone, String secondaryCareTakerPhone);
}
