package com.omdbapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieData {
    @JsonProperty("Actors")
    private String actors;

    public String getActors() {
        return actors;
    }

    @JsonProperty("Director")
    private String director;

    public String getDirector() {
        return director;
    }

    @JsonProperty("Country")
    private String country;

    public String getCountry() {
        return country;
    }
}
