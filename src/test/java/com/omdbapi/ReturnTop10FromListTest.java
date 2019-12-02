package com.omdbapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.get;

public class ReturnTop10FromListTest {
    public static final String OMDBAPI = "https://www.omdbapi.com/?apikey=cac8f4c5&t=";

    public static final String ACTORS = "actors";
    public static final String CONTRIES = "contries";
    public static final String DIRECTORS = "directors";
    private ObjectMapper objectMapper;

    @DataProvider(name = "searchData")
    public Object[][] getDataFromDataprovider() {
        return new Object[][]
                {
                        {ACTORS},
                        {CONTRIES},
                        {DIRECTORS},
                };
    }

    @Test(dataProvider = "searchData")
    public void returnTop10FromListTest(String searchCriteria) throws Exception {
        objectMapper = new ObjectMapper();
        String prepareData = prepareData("movies.json");
        MovieSet movieSet = objectMapper.readValue(prepareData, MovieSet.class);
        List<String> findValues = movieSet.getMovies().stream().map(expMovie -> {
            MovieData movieData = get(OMDBAPI + expMovie)
                    .body()
                    .as(MovieData.class);
            if (ACTORS.equals(searchCriteria)) {
                return Arrays.asList(movieData.getActors().split(","));
            }
            if (CONTRIES.equals(searchCriteria)) {
                return Arrays.asList(movieData.getCountry().split(","));
            }
            if (DIRECTORS.equals(searchCriteria)) {
                return Arrays.asList(movieData.getDirector().split(","));
            }
            throw new RuntimeException("No search criteria provided");
        })
                .reduce(new ArrayList<>(), combiner);

            HashMap<String, Integer> listOfParticipation = new HashMap<>();
            for (String participant : findValues) {
                if (listOfParticipation.containsKey(participant)) {
                    listOfParticipation.put(participant, listOfParticipation.get(participant) + 1);
                } else {
                    listOfParticipation.put(participant, 1);
                }
            }
            LinkedHashMap<String, Integer> collect = listOfParticipation.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            System.out.println(collect);
        }


        private String prepareData (String sampleDataFileLocation) throws Exception {
            final URL resource = this.getClass().getClassLoader().getResource(sampleDataFileLocation);
            final Path path = Paths.get(Objects.requireNonNull(resource).toURI());
            StringBuilder data = new StringBuilder();
            Files.lines(path).forEach(line -> data.append(line.trim()));
            return data.toString();
        }

        private BinaryOperator<List<String>> combiner = (strings, strings2) -> {
            strings.addAll(strings2);
            return strings;
        };
    }
