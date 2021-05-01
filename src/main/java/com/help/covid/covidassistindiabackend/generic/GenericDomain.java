package com.help.covid.covidassistindiabackend.generic;

public interface GenericDomain<T extends GenericEntity> {
    T toEntity();
}