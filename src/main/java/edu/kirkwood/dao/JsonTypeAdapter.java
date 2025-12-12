package edu.kirkwood.dao;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class JsonTypeAdapter {
    /**
     * Used by Gson when a date is formatted like 'YYYY-MM-DD' converts Json to LocalDate object
     */
    public static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return LocalDate.parse(json.getAsString());
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }


    /**

     * Used by Gson when date is formatted like "1967-07-23T09:18:33"

     */

    public static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

        @Override

        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

            try {

                return LocalDateTime.parse(json.getAsString());

            } catch (DateTimeParseException e) {

                return null;

            }

        }

    }



    /**

     * Used by Gson when date is formatted like "1967-07-23T09:18:33.666Z"

     */

    public static class InstantDeserializer implements JsonDeserializer<Instant> {

        @Override

        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

            try {

                return Instant.parse(json.getAsString());

            } catch (DateTimeParseException e) {

                return null;

            }

        }

    }

}
