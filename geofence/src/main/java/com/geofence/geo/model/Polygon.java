package com.geofence.geo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.UUID;

@Document
public class Polygon {
    @Id
    @Schema(description = "An Auto-generated value for the polygon id", example = "1ffa52ea-fc26-4c56-8091-f11f76b96ede", required = true)
    String id;
    @Schema(description = "A set of Coordinates defining the Geofence polygon",example = "[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]", required=true)
    Coordinates[] coordinates;
    @Schema(description = "A String name for the Geofence",example = "Cessna", required=true)
    String name;
    String tenantId;
    int proximity;

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public Polygon(Polygon polygon) {
        id = UUID.randomUUID().toString();
        this.coordinates = polygon.coordinates;
        this.name=polygon.name;
        this.tenantId=polygon.tenantId;
        this.proximity=polygon.proximity;

    }

    public Polygon(Coordinates[] coordinates, String name, String tenantId, int proximity) {
        id = UUID.randomUUID().toString();
        this.coordinates = coordinates;
        this.name = name;
        this.tenantId = tenantId;
        this.proximity = proximity;
    }

    public Polygon(String id,Coordinates[] coordinates,String name,String tenantId) {
        this.id = id;
        this.coordinates = coordinates;
        this.name = name;
        this.tenantId = tenantId;

    }

    public Polygon(String id,Coordinates[] coordinates,String name,int proximity) {
        this.id = id;
        this.coordinates = coordinates;
        this.name = name;
        this.proximity=proximity;

    }

    public Polygon() {
    }
    public Polygon(Coordinates[]coordinates) {
        this.coordinates=coordinates;
    }


    public String getId() {
        return id;
    }

    public Coordinates[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", coordinates:" + Arrays.toString(coordinates) +
                ", name:'" + name + '\'' +
                ", tenantId:'" + tenantId + '\'' +
                ", proximity=" + proximity +
                '}';
    }
}
