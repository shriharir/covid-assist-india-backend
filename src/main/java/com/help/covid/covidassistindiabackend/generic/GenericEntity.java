package com.help.covid.covidassistindiabackend.generic;

public interface GenericEntity<T extends GenericDomain> {
    T toDomain();
}
