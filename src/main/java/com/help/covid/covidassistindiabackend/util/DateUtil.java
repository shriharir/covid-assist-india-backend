package com.help.covid.covidassistindiabackend.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.help.covid.covidassistindiabackend.exception.InvalidDateException;
import org.springframework.stereotype.Component;

import static java.time.LocalDate.now;

@Component
public class DateUtil {

    public LocalDate parse(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw InvalidDateException.invalidDateFormat();
        }
    }

    public LocalDate parseOrDefaultNow(String date) {
        if (date == null) {
            return now();
        }
        return parse(date);
    }

    public LocalDate parseOrDefault180DaysLess(String fromDateAsString, LocalDate toDate) {
        if (fromDateAsString == null) {
            return toDate.minusDays(180);
        }
        return parse(fromDateAsString);
    }
}
