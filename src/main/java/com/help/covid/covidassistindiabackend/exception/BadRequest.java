package com.help.covid.covidassistindiabackend.exception;

public class BadRequest extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String BAD_GENERIC_REQUEST = "Data is empty";
    private static final String BAD_DATA_TYPE_REQUESTED = "Data contains invalid key";
    private static final String VOLUNTEER_NOT_ASSIGNED_TO_REQUEST = "Volunteer not assigned to the request.";

    private BadRequest(String message) {
        super(message);
    }

    public static BadRequest badRequestForGenericRequest() {
        return new BadRequest(BAD_GENERIC_REQUEST);
    }

    public static BadRequest badRequestForInvalidDataType() {
        return new BadRequest(VOLUNTEER_NOT_ASSIGNED_TO_REQUEST);
    }

    public static BadRequest volunteerNotAssignedToRequest() {
        return new BadRequest(VOLUNTEER_NOT_ASSIGNED_TO_REQUEST);
    }


}