package com.geofence.geo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Document
public class Marker {
    @Id
    public String markerID;
    private Coordinates markerPosition;
    private String currentState;
    private String geofenceID;
    private String geofenceName;
    private  String timeStamp;
    private Double distance;
    Long dwellTime;
    Long geofenceEnterTime;
    boolean dwellActive;

    public boolean isDwellActive() {
        return dwellActive;
    }

    public void setDwellActive(boolean dwellActive) {
        this.dwellActive = dwellActive;
    }


    public Long getGeofenceEnterTime() {
        return geofenceEnterTime;
    }

    public void setGeofenceEnterTime(Long geofenceEnterTime) {
        this.geofenceEnterTime = geofenceEnterTime;
    }
    public Long getDwellTime() {
        return dwellTime;
    }

    public void setDwellTime(Long dwellTime) {
        this.dwellTime = dwellTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    public Marker(){
        markerID=UUID.randomUUID().toString();
    }
    public Marker(String markerID,Coordinates markerPosition, String currentState, String geofenceID, String timeStamp,String geofenceName) {
        this.markerID=markerID;
        this.markerPosition = markerPosition;
        this.currentState = currentState;
        this.geofenceID = geofenceID;
        this.timeStamp = timeStamp;
        this.geofenceEnterTime= Long.valueOf(0);
        this.dwellTime= Long.valueOf(0);
        this.dwellActive=true;
        this.geofenceName=geofenceName;
    }
    public Marker(Coordinates markerPosition, String currentState, String geofenceID, String timeStamp) {
        markerID=UUID.randomUUID().toString();
        this.markerPosition = markerPosition;
        this.currentState = currentState;
        this.geofenceID = geofenceID;
        this.timeStamp = timeStamp;
        this.geofenceEnterTime= Long.valueOf(0);
        this.dwellTime= Long.valueOf(0);
        this.distance= Double.valueOf(0);
        this.dwellActive=true;
    }
    public Marker(String markerID,Coordinates markerPosition, String currentState,  String timeStamp,String geofenceName) {
        this.markerID=markerID;
        this.markerPosition = markerPosition;
        this.currentState = currentState;
        this.timeStamp = timeStamp;
        this.geofenceEnterTime= Long.valueOf(0);
        this.dwellTime= Long.valueOf(0);
        this.distance= Double.valueOf(0);
        this.dwellActive=true;
        this.geofenceName=geofenceName;
    }

    public Coordinates getMarkerPosition() {
        return markerPosition;
    }

    public void setMarkerPosition(Coordinates markerPosition) {
        this.markerPosition = markerPosition;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getGeofenceID() {
        return geofenceID;
    }

    public void setGeofenceID(String geofenceID) {
        this.geofenceID = geofenceID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String toString() {
        return "Marker{" +
                "markerID='" + markerID + '\'' +
                ", markerPosition=" + markerPosition +
                ", currentState='" + currentState + '\'' +
                ", geofenceID='" + geofenceID + '\'' +
                ", geofenceName='" + geofenceName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", distance=" + distance +
                ", dwellTime=" + dwellTime +
                ", geofenceEnterTime=" + geofenceEnterTime +
                '}';
    }
}
