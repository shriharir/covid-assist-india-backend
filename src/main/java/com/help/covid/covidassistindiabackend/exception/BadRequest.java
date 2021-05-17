package com.help.covid.covidassistindiabackend.exception;

public class BadRequest extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String BAD_GENERIC_REQUEST = "Data is empty";
    private static final String BAD_DATA_TYPE_REQUESTED = "Data contains invalid key";
    private static final String VOLUNTEER_NOT_ASSIGNED_TO_REQUEST = "Volunteer not assigned to the request.";
    private static final String REQUEST_ASSIGNED_TO_DIFFERENT_VOLUNTEER = "Request Assigned to different volunteer";
    private static final String REQUEST_ALREADY_EXISTS = "Request Already exists with given srfId or buNumber or Primary/Secondary phone number";

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

    public static BadRequest requestAssignedToDifferentVolunteer() {
        return new BadRequest(REQUEST_ASSIGNED_TO_DIFFERENT_VOLUNTEER);
    }

    public static BadRequest requestAlreadyExists() {
        return new BadRequest(REQUEST_ALREADY_EXISTS);
    }
}