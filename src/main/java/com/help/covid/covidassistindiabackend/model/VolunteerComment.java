package com.help.covid.covidassistindiabackend.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.help.covid.covidassistindiabackend.generic.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class VolunteerComment implements Serializable {
    public String comment;
    public String volunteerId;
    public ZonedDateTime eventTime;
}
