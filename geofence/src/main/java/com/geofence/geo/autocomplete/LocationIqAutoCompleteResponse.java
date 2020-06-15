package com.geofence.geo.autocomplete;

import com.geofence.geo.model.autocompleteproviders.GenericAutocomplete;
import com.geofence.geo.model.autocompleteproviders.LocationIqAutocomplete;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationIqAutoCompleteResponse implements AutoCompleteResponse {
    @Value("${locationiq.autocomplete.url}")
    String urlName;
    @Value("${locationiq.autocomplete.api_key}")
    String apiKey;


    @Override
    public List<GenericAutocomplete> getAutoCompleteResponse(String locationQuery) throws IOException {
        URL locationIqUrl = new URL(urlName + "?key=" + apiKey + "&q=" + locationQuery);
        HttpURLConnection connection = (HttpURLConnection) locationIqUrl.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();

        if (status != 200) {
            return new ArrayList<>();
        }

        BufferedReader in;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        String autoCompletePredictions = content.toString();
        Gson gson = new Gson();
        LocationIqAutocomplete[] locationIqAutocompleteList = gson.fromJson(autoCompletePredictions, LocationIqAutocomplete[].class);
        int noOfResponses = Math.min(5, locationIqAutocompleteList.length);
        LocationIqAutocomplete[] locationIqAutocompletesFinal = new LocationIqAutocomplete[noOfResponses];
        System.arraycopy(locationIqAutocompleteList, 0, locationIqAutocompletesFinal, 0, noOfResponses);
        List<LocationIqAutocomplete> locationIQResponses = Arrays.asList(locationIqAutocompletesFinal);
        return locationIQResponses.stream().map(n -> {
            GenericAutocomplete genericAutocomplete = new GenericAutocomplete();
            genericAutocomplete.setDisplayName(n.getDisplayName());
            genericAutocomplete.setPlaceId(n.getPlaceId());
            genericAutocomplete.setLatitude(n.getLat());
            genericAutocomplete.setLongitude(n.getLon());
            return genericAutocomplete;
        }).collect(Collectors.toList());
    }
}
