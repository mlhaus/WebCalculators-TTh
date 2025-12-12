package edu.kirkwood.controller;

import edu.kirkwood.dao.MovieDAO;
import edu.kirkwood.dao.MovieDAOFactory;
import edu.kirkwood.dao.impl.JsonMovieDAO;
import edu.kirkwood.dao.impl.MySQLMovieDAO;
import edu.kirkwood.dao.impl.XmlMovieDAO;
import edu.kirkwood.model.Movie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/movies")
public class MovieServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("title");
        List<Movie> movies = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            try {
                movies = getResults(search);
                if (movies.isEmpty()) {
                    req.setAttribute("searchError", "No results found.");
                }
            } catch (RuntimeException e) {
                req.setAttribute("searchError", e.getMessage());
            }
        }
        req.setAttribute("movies", movies);
        req.getRequestDispatcher("WEB-INF/movies.jsp").forward(req,resp);    }

    public static List<Movie> getResults(String title) {
        MovieDAO movieDAO = MovieDAOFactory.getMovieDAO();
        List<Movie> results = new ArrayList<>();
        if (movieDAO instanceof XmlMovieDAO) {
            XmlMovieDAO xmlMovieDAO = (XmlMovieDAO) movieDAO;
            results = xmlMovieDAO.search(title);
        } else if (movieDAO instanceof MySQLMovieDAO) {
            MySQLMovieDAO mySQLMovieDAO = (MySQLMovieDAO) movieDAO;
            results = mySQLMovieDAO.search(title);
        } else if (movieDAO instanceof JsonMovieDAO) {
            JsonMovieDAO jsonMovieDAO = (JsonMovieDAO) movieDAO;
            results = jsonMovieDAO.search(title);
        }

        try {

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return results;
    }
}
