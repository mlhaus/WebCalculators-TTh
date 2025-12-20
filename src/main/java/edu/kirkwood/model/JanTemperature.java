package edu.kirkwood.model;

/**
 * Represents a temperature value and provides utility methods for
 * converting between Celsius, Fahrenheit, and Kelvin scales.
 * This class ensures that no temperature below absolute zero can be created.
 */
public class JanTemperature {

    private double degrees;
    private final char scale; // 'C', 'F', or 'K'

    public JanTemperature() {
        this.degrees = 0;
        this.scale = 'C';
    }

    public JanTemperature(double degrees, char scale) {
        if (!isValidScale(scale)) {
            throw new IllegalArgumentException("Invalid scale: Must be 'C', 'F', or 'K'.");
        }
        if (isBelowAbsoluteZero(degrees, scale)) {
            throw new IllegalArgumentException("Temperature cannot be below absolute zero.");
        }
        this.degrees = degrees;
        this.scale = Character.toUpperCase(scale);
    }

    public double toCelsius() {
        if (scale == 'C') return degrees;
        if (scale == 'F') return (degrees - 32) * 5.0 / 9.0;
        return degrees - 273.15;
    }

    public double toFahrenheit() {
        if (scale == 'F') return degrees;
        if (scale == 'C') return (degrees * 9.0 / 5.0) + 32;
        return (degrees - 273.15) * 9.0 / 5.0 + 32;
    }

    public double toKelvin() {
        if (scale == 'K') return degrees;
        if (scale == 'C') return degrees + 273.15;
        return (degrees - 32) * 5.0 / 9.0 + 273.15;
    }

    private boolean isValidScale(char s) {
        char upper = Character.toUpperCase(s);
        return upper == 'C' || upper == 'F' || upper == 'K';
    }

    private boolean isBelowAbsoluteZero(double deg, char s) {
        char upper = Character.toUpperCase(s);
        if (upper == 'C') return deg < -273.15;
        if (upper == 'F') return deg < -459.67;
        if (upper == 'K') return deg < 0;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", degrees, scale);
    }
}