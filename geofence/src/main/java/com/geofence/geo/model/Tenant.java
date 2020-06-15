package com.geofence.geo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Document

public class Tenant implements UserDetails {
    @Id
    @Schema(description = "An Auto-generated value for Tenant id", example = "1ffa52ea-fc26-4c56-8091-f11f76b96ede", required = true)
    String id;
    @Schema(description = "A String name for the Tenant", example = "Dummy", required = true)
    String name;
    String password;
    String autocompleteProvider;

    public Tenant(String name, String password, String autocompleteProvider) {
        this.name = name;
        id = UUID.randomUUID().toString();
        this.password = password;
        this.autocompleteProvider = autocompleteProvider;
    }

    public String getAutocompleteProvider() {
        return autocompleteProvider;
    }

    public void setAutocompleteProvider(String autocompleteProvider) {
        this.autocompleteProvider = autocompleteProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Tenant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", autocompleteProvider='" + autocompleteProvider + '\'' +
                '}';
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
