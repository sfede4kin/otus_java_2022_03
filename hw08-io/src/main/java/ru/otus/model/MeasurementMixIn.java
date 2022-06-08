package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MeasurementMixIn {
    MeasurementMixIn(@JsonProperty("name") String name,
                @JsonProperty("value") double value){
    }
}
