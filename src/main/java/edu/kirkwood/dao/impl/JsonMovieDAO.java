package edu.kirkwood.dao.impl;

import com.google.gson.*;
import edu.kirkwood.dao.JsonTypeAdapter;
import edu.kirkwood.dao.MovieDAO;
import edu.kirkwood.model.Movie;
import edu.kirkwood.model.json.TmdbMovieResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonMovieDAO implements MovieDAO {
    private String apiURL;
    private String apiToken;
    public JsonMovieDAO(String apiURL, String apiToken) {
        this.apiURL = apiURL;
        this.apiToken = apiToken;
    }

    /**
     * Get raw data from themoviedb API
     * @param title The movie title to search for
     * @param page The page number to search for
     * @return A json String containing all matching movies
     */
    public String getRawData(String title, int page) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiURL + "query=" + title +  "&page=" + page)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();
        Response response = null;
        String responseBody = "";
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseBody;
    }


    /**
     * Retrieves all movies from the data source that matches the title
     *
     * @param title The movie title a user is searching for
     * @return A list of movies that matches the searched title
     */
    @Override
    public List<Movie> search(String title) {
        List<Movie> movies = new ArrayList<>();
        int currentPage = 1;
        while(true) {
            String rawData = getRawData(title, currentPage);
//        System.out.println(prettyFormatter(rawData));
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new JsonTypeAdapter.LocalDateDeserializer())
                    .create();
            TmdbMovieResponse movieResponse = null;
            try {
                movieResponse = gson.fromJson(rawData, TmdbMovieResponse.class);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
            movieResponse.getResults().forEach(result -> {
                Movie movie = new Movie();
                movie.setId(result.getId());
                movie.setTitle(result.getTitle());
                if (result.getRelease_date() != null) {
                    movie.setYear(result.getRelease_date().getYear());
                }
                movie.setPlot(result.getOverview());
                movies.add(movie);
            });
            if (movieResponse.getTotal_pages() > currentPage) {
                currentPage++;
            } else {break;}
        }
        return movies;
    }

    /**
     * To format raw json data into a human-readable format
     * @param rawData The unformatted json data
     * @return A human-readable String of json
     */
    public String prettyFormatter(String rawData) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonElement jsonElement = new JsonParser().parse(rawData);

        return gson.toJson(jsonElement);
    }
}
