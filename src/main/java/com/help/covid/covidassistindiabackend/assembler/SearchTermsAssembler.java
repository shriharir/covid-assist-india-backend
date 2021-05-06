package com.help.covid.covidassistindiabackend.assembler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import com.help.covid.covidassistindiabackend.model.DateRange;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import com.help.covid.covidassistindiabackend.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class SearchTermsAssembler {

    private final DateUtil dateUtil;

    public SearchTerms assemble(Integer pageNumber, Integer limit, String from, String to, List<String> status, List<String> serviceTypes, String volunteerId) {
        LocalDate toDate = dateUtil.parseOrDefaultNow(to);
        LocalDate fromDate = dateUtil.parseOrDefault180DaysLess(from, toDate);

        DateRange dateRange = DateRange.builder()
                .to(ZonedDateTime.of(toDate, LocalTime.MAX, UTC))
                .from(ZonedDateTime.of(fromDate, LocalTime.MIN, UTC))
                .build();
        //status.replaceAll(String::toUpperCase);
        //serviceTypes.replaceAll(String::toUpperCase);
        return SearchTerms.builder()
                .pageNumber(pageNumber)
                .limit(limit)
                .dateRange(dateRange)
                .statuses(status)
                .serviceTypes(serviceTypes)
                .volunteerId(volunteerId)
                .build();
    }
}
