package com.geofence.geo.exception;

public class InvalidLoginResponse {
    private String name;
    private String password;

    public InvalidLoginResponse() {
        this.name = "Invalid Username";
        this.password = "Invalid Password";
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
