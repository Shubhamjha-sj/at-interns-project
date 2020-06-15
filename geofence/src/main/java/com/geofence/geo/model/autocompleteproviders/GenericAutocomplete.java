package com.geofence.geo.model.autocompleteproviders;

public class GenericAutocomplete {
    private String placeId;
    private String displayName;
    private Double latitude;
    private Double longitude;

    public GenericAutocomplete() {
        //Defalut no-args constructor
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GenericAutocomplete{" +
                "placeId='" + placeId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
