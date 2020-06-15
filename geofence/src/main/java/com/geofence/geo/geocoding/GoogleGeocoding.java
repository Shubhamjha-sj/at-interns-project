package com.geofence.geo.geocoding;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleGeocoding {

    @Autowired
    Environment env;
    public LatLng getCoordinatesFromPlaceId(String placeId) throws InterruptedException, ApiException, IOException {

        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(env.getProperty("google.autocomplete.api_key")).build();
        PlaceDetails placeDetails = PlacesApi.placeDetails(geoApiContext, placeId).await();




        return placeDetails.geometry.location;
    }
}
