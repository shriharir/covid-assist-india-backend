package com.help.covid.covidassistindiabackend.exception;

public class BadRequest extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String BAD_GENERIC_REQUEST = "Data is empty";
    private static final String BAD_DATA_TYPE_REQUESTED = "Data contains invalid key";
    public static final String INVALID_CHANNEL = "Invalid value for channel";
    public static final String MISSING_AUTHORIZATION = "Missing mandatory header attribute authorization";

    private BadRequest(String message) {
        super(message);
    }

    public static BadRequest badRequestForGenericRequest() {
        return new BadRequest(BAD_GENERIC_REQUEST);
    }

    public static BadRequest badRequestForInvalidDataType() {
        return new BadRequest(BAD_DATA_TYPE_REQUESTED);
    }

    public static BadRequest invalidChannel() {
        return new BadRequest(INVALID_CHANNEL);
    }

    public static BadRequest missingAuthorization() {
        return new BadRequest(MISSING_AUTHORIZATION);
    }

}