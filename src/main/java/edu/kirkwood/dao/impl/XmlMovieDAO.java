package edu.kirkwood.dao.impl;

import edu.kirkwood.dao.MovieDAO;
import edu.kirkwood.model.Movie;
import edu.kirkwood.model.xml.MovieSearchResult;
import edu.kirkwood.model.xml.OmdbMovieResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XmlMovieDAO implements MovieDAO {
    private String apiURL;

    public XmlMovieDAO(String apiURL) {
        this.apiURL = apiURL;
    }

    /**
     * Retrieves all movies from the data source that matches the title
     * @param title The movie title a user is searching for
     * @return A List<Movie> movies that matches the search title
     */
    @Override
    public List<Movie> search(String title) {
        List<MovieSearchResult> results = fetch(title);
        List<Movie> movies = new ArrayList<>();
        results.forEach(result -> {
            Movie movie = new Movie();
            movie.setId(result.getId());
            movie.setTitle(result.getTitle());
            movie.setYear(result.getYear());
            // The XML data source does not include a plot
            movies.add(movie);
        });
        return movies;
    }

    /**
     * Retrieves all movies from the data source that matches the title
     * @param title The movie title a user is searching for
     * @return A List<MovieSearchResult> movies that matches the search title
     */
    public List<MovieSearchResult> fetch(String title) {
        if(apiURL == null || apiURL.isEmpty()) {
            throw new IllegalArgumentException("apiURL cannot be null or empty");
        }

        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        int page = 1;
        String fullURL = String.format("%s&s=%s&page=%s", apiURL, encodedTitle, page);
        List<MovieSearchResult> results = new ArrayList<>();
        boolean finished = false;
        while(!finished) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fullURL))
                        .build();
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                OmdbMovieResponse omdbMovieResponse = parseXml(body);
                boolean dataReturned = omdbMovieResponse.getResponse().equalsIgnoreCase("true") ? true : false;
                if(!dataReturned) {
                    break;
                }
                results.addAll(omdbMovieResponse.getSearchResults());
                page++;
                fullURL = String.format("%s&s=%s&page=%s", apiURL, encodedTitle, page);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    /**
     * Parse an XML file into a list of movies
     * @param xml The raw String data
     * @return a List of MovieSearchResult objects
     */
    public OmdbMovieResponse parseXml(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(OmdbMovieResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        OmdbMovieResponse movieResponse = (OmdbMovieResponse)unmarshaller.unmarshal(reader);
        return movieResponse;
    }

}
