package com.omdbapi;

import java.util.List;
import java.util.Objects;

public class MovieSet {
    private List<String> movies;

    public List<String> getMovies() {
        return movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieSet)) return false;
        MovieSet movieSet = (MovieSet) o;
        return getMovies().equals(movieSet.getMovies());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMovies());
    }
}
