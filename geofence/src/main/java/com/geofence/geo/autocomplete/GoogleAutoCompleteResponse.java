package com.geofence.geo.autocomplete;

import com.geofence.geo.model.autocompleteproviders.GenericAutocomplete;
import com.geofence.geo.model.autocompleteproviders.GoogleAutocomplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.PlaceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoogleAutoCompleteResponse implements AutoCompleteResponse {

    @Value("${google.autocomplete.api_key}")
    private String googleApiKey;
    Logger logger = LoggerFactory.getLogger("Logger for GoogleAutoCompleteResponse class");
    private GeoApiContext geoApiContext;

    public GoogleAutocomplete setGoogleLatLng(GoogleAutocomplete googleAutocomplete) throws InterruptedException, ApiException, IOException {
        geoApiContext = new GeoApiContext.Builder().apiKey(googleApiKey).build();
        PlaceDetails placeDetails = PlacesApi.placeDetails(geoApiContext, googleAutocomplete.getPlaceId()).await();
        googleAutocomplete.setCoordinates(placeDetails.geometry.location);
        return googleAutocomplete;
    }

    @Override
    public List<GenericAutocomplete> getAutoCompleteResponse(String locationQuery) throws InterruptedException, ApiException, IOException {
        geoApiContext = new GeoApiContext.Builder().apiKey(googleApiKey).build();
        PlaceAutocompleteRequest.SessionToken sessionToken = new PlaceAutocompleteRequest.SessionToken();
        final AutocompletePrediction[] predictions = PlacesApi.placeAutocomplete(geoApiContext, locationQuery, sessionToken).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GoogleAutocomplete[] googleAutocompletes = gson.fromJson(gson.toJson(predictions), GoogleAutocomplete[].class);
        List<GenericAutocomplete> genericAutocompletes;
        List<GoogleAutocomplete> googleAutocompleteList = Arrays.asList(googleAutocompletes);
        googleAutocompleteList = googleAutocompleteList.stream().map(n -> {
            try {
                return setGoogleLatLng(n);
            } catch (InterruptedException | ApiException | IOException e) {
                logger.error("context", e);
                Thread.currentThread().interrupt();
            }
            return n;
        }).collect(Collectors.toList());
        genericAutocompletes = googleAutocompleteList.stream().map(n -> {
            GenericAutocomplete genericAutocomplete = new GenericAutocomplete();
            genericAutocomplete.setDisplayName(n.getDescription());
            genericAutocomplete.setPlaceId(n.getPlaceId());
            genericAutocomplete.setLatitude(n.getCoordinates().lat);
            genericAutocomplete.setLongitude(n.getCoordinates().lng);
            return genericAutocomplete;
        }).collect(Collectors.toList());
       return genericAutocompletes;
    }
}
