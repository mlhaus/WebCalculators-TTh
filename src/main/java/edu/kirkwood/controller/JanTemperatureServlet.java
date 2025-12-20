package edu.kirkwood.controller;

import edu.kirkwood.model.JanTemperature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Make sure you have the 'isANumber' helper imported.
// If it's in a different package, update this import.
import static edu.kirkwood.shared.Helpers.isANumber;

@WebServlet(value="/jan-temperature")
public class JanTemperatureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/jan-temperature.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String degreesStr = req.getParameter("degrees");
        String currentScale = req.getParameter("currentScale");
        String targetScale = req.getParameter("targetScale");

        // Preserve user input
        req.setAttribute("degrees", degreesStr);
        req.setAttribute("currentScale", currentScale);
        req.setAttribute("targetScale", targetScale);

        boolean errorsFound = false;

        // Validation 1: Check if degrees is empty or not a number
        if(degreesStr == null || degreesStr.isEmpty()){
            req.setAttribute("degreesError", "The temperature value is required");
            errorsFound = true;
        } else if(!isANumber(degreesStr)) {
            req.setAttribute("degreesError", "The temperature must be a valid number");
            errorsFound = true;
        }

        // Validation 2: Check scales
        String[] validScales = {"C", "F", "K"};
        List<String> validScalesList = Arrays.asList(validScales);

        if (currentScale == null || !validScalesList.contains(currentScale)) {
            req.setAttribute("currentScaleError", "Invalid current scale selected.");
            errorsFound = true;
        }
        if (targetScale == null || !validScalesList.contains(targetScale)) {
            req.setAttribute("targetScaleError", "Invalid target scale selected.");
            errorsFound = true;
        }

        if(!errorsFound) {
            try {
                double degrees = Double.parseDouble(degreesStr);
                char cScale = currentScale.charAt(0);
                char tScale = targetScale.charAt(0);

                // Use the renamed model class
                JanTemperature temp = new JanTemperature(degrees, cScale);

                double resultValue = 0;
                String symbol = "";

                switch (tScale) {
                    case 'C':
                        resultValue = temp.toCelsius();
                        symbol = "°C";
                        break;
                    case 'F':
                        resultValue = temp.toFahrenheit();
                        symbol = "°F";
                        break;
                    case 'K':
                        resultValue = temp.toKelvin();
                        symbol = "K";
                        break;
                }

                String result = String.format("%.2f %s = %.2f %s", degrees, cScale, resultValue, symbol);
                req.setAttribute("result", result);

            } catch (IllegalArgumentException e) {
                // Catches "Below Absolute Zero" errors from the Model
                req.setAttribute("degreesError", e.getMessage());
            } catch (Exception e) {
                req.setAttribute("degreesError", "An unexpected error occurred.");
            }
        }

        req.getRequestDispatcher("WEB-INF/jan-temperature.jsp").forward(req, resp);
    }
}