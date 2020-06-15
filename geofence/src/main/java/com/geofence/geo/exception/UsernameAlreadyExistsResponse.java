package com.geofence.geo.exception;

public class UsernameAlreadyExistsResponse {

    private String name;

    public UsernameAlreadyExistsResponse(String username) {
        this.name = username;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }
}