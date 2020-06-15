package com.geofence.geo.controller;

import com.geofence.geo.model.Coordinates;
import com.geofence.geo.model.Marker;
import com.geofence.geo.security.JwtTokenProvider;
import com.geofence.geo.service.CheckPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Check Position", description = "APIs to check coordinate position")
@State(Scope.Benchmark)
@Component
@RequestMapping("/position")
public class CheckPositionController {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    CheckPositionService checkPositionService;

    @Operation(summary = "Create a new Marker", description = "Create a new Marker to be tracked", tags = {"marker"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marker Created",
                    content = @Content(schema = @Schema(implementation = Coordinates.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @Benchmark
    @PostMapping(value = "/createMarker")
    public ResponseEntity<String> createMarker(@RequestBody String markerInfo, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(checkPositionService.savePosition(tenantId, markerInfo), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Check Marker Position",
            description = "Get Position of the marker to determine if it is Inside/Outside the Geofence", tags = {"marker"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marker Position retrieved",
                    content = @Content(schema = @Schema(implementation = Coordinates.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping(value = "/checkPosition")
    public ResponseEntity<String> checkPosition(@Parameter(required = true, content = @Content(schema = @Schema(implementation = Marker.class)))
                                              @RequestParam(defaultValue = "-1") Integer proximity,
                                              @RequestBody String markerInfo,
                                              @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(checkPositionService.getPosition(tenantId, markerInfo, proximity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get All Markers' position",
            description = "Get Position of all the marker passed to determine if they are Inside/Outside the Geofence",
            tags = {"marker"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marker Positions retrieved",
                    content = @Content(schema = @Schema(implementation = Marker[].class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping(value = "/checkAllPositions", produces = "application/json")
    public ResponseEntity<String> checkAllPositions(@RequestBody String markersInfo, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(checkPositionService.getAllMarkerStates(tenantId, markersInfo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Selective Markers' position",
            description = "Get Position of all the marker passed based on whether they are Inside/Outside the Geofence",
            tags = {"marker"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marker Positions retrieved",
                    content = @Content(schema = @Schema(implementation = Marker[].class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping(value = "/filterByState", produces = "application/json")
    public ResponseEntity<String> filterByState(@RequestParam String selector, @RequestBody String markersInfo,
                                                      @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(checkPositionService.getSelectiveMarkers(tenantId, markersInfo, selector), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
