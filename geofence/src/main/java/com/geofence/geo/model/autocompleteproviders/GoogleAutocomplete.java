package com.geofence.geo.model.autocompleteproviders;

import com.google.maps.model.LatLng;

public class GoogleAutocomplete {
    private String placeId;
    private String description;
    private LatLng coordinates;

    public GoogleAutocomplete() {
        //Default no-args constructor
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "GoogleAutocomplete{" +
                "placeId='" + placeId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
