package edu.kirkwood.dao;

import edu.kirkwood.dao.impl.JsonMovieDAO;
import edu.kirkwood.dao.impl.MySQLMovieDAO;
import edu.kirkwood.dao.impl.XmlMovieDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MovieDAOFactory {
    private static Properties properties =  new Properties();
    static {
        try(InputStream is = MovieDAOFactory.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if(is == null) {
                throw new RuntimeException("application.properties file not found");
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To retrieve a MovieDAO based on application.properties settings
     * @return A MovieDAO (XMLMovieDAO, MySQLMovieDAO, JSONMovieDAO, etc.)
     */
    public static MovieDAO getMovieDAO() {
        String dataSourceType = properties.getProperty("datasource.type");

        if(dataSourceType == null || dataSourceType.isEmpty()) {
            throw new IllegalArgumentException("dataSourceType cannot be empty");
        }
        switch (dataSourceType.toLowerCase()) {
            case "xml":
                String apiURL = properties.getProperty("xml.apiURL");
                if(apiURL == null || apiURL.isEmpty()) {
                    throw new IllegalArgumentException("xml.apiURL cannot be empty");
                }
                return new XmlMovieDAO(apiURL);
            // To be implemented:
            case "json":
                String jsonApiURL = properties.getProperty("json.apiURL");
                String jsonApiToken = properties.getProperty("json.apiReadAccessToken");
                if(jsonApiURL == null || jsonApiURL.isEmpty()) {
                    throw new IllegalArgumentException("json.apiURL cannot be empty");
                }
                if(jsonApiToken == null || jsonApiToken.isEmpty()) {
                    throw new IllegalArgumentException("json.apiReadAccessToken cannot be empty");
                }
                return new JsonMovieDAO(jsonApiURL, jsonApiToken);
            case "mysql":
                String connectionString = properties.getProperty("mysql.connectionString");
                if(connectionString == null || connectionString.isEmpty()) {
                    throw new IllegalArgumentException("mysql connection string cannot be empty");
                }
                return new MySQLMovieDAO();
//            case "mongodb":
//                break;
            default:
                throw new RuntimeException("Unsupported data source type: " + dataSourceType);
        }
    }

    public static void main(String[] args) {
        MovieDAOFactory.getMovieDAO();
    }
}
