package com.help.covid.covidassistindiabackend.model;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class DateRange {
    private ZonedDateTime from;
    private ZonedDateTime to;
}
