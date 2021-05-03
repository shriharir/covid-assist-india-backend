package com.help.covid.covidassistindiabackend.model;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestStatus {
    public String status;
    public String volunteerId;
    public String updatedBy;
    public ZonedDateTime eventTime;
    public String comments;
}
