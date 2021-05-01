package com.help.covid.covidassistindiabackend.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7174107515849248100L;

    public static final String REQUEST_NOT_FOUND = "Request not found";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException requestNotFund() {
        return new ResourceNotFoundException(REQUEST_NOT_FOUND);
    }

}

