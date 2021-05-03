package com.help.covid.covidassistindiabackend.exception;

public class InvalidDateException extends RuntimeException {
    private static final long serialVersionUID = 8234107515849248100L;
    private static final String INVALID_DATE_FORMAT = "Invalid date format(YYYY-MM-DD)";
    private static final String INVALID_DATE_RANGE = "From date must be before to date";


    private InvalidDateException(String message) {
        super(message);
    }
    public static InvalidDateException invalidDateFormat(){
        return new InvalidDateException(INVALID_DATE_FORMAT);
    }

    public static InvalidDateException invalidDateRange(){
        return new InvalidDateException(INVALID_DATE_RANGE);
    }


}
