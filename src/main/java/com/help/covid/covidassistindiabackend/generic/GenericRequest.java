package com.help.covid.covidassistindiabackend.generic;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.help.covid.covidassistindiabackend.exception.BadRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericRequest<T> implements CoreDTO {

    private List<String> validDataTypeAllowed = List.of("patientAssistRequest");

    @JsonIgnore
    public T getInlineData() {
        if (data == null) {
            throw BadRequest.badRequestForGenericRequest();
        }
        validDataTypeAllowed
                .stream()
                .filter(dataType -> data.containsKey(dataType))
                .findFirst()
                .orElseThrow(BadRequest::badRequestForInvalidDataType);
        return this.data.values().stream().findFirst()
                .orElseThrow(BadRequest::badRequestForGenericRequest);
    }

    @NotNull
    private Map<String, @Valid T> data;

    public Map<String, T> getData() {
        return data;
    }

    public void setData(Map<String, T> data) {
        this.data = data;
    }
}
