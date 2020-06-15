package com.geofence.geo.model.autocompleteproviders;

import com.google.maps.model.LatLng;

public class LocationIqAutocomplete {

    private String place_id;
    private String display_name;
    private Double lat;
    private Double lon;

    public LocationIqAutocomplete(String place_id, String display_name, Double lat, Double lon) {
        this.place_id = place_id;
        this.display_name = display_name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public String getDisplayName() {
        return display_name;
    }

    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public LatLng getLocationCoordinates() {
        return new LatLng(this.lat, this.lon);
    }
}
