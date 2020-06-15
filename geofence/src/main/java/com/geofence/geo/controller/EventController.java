package com.geofence.geo.controller;

import com.geofence.geo.events.Events;
import com.geofence.geo.security.JwtTokenProvider;
import com.geofence.geo.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@Tag(name = "Event", description = "Event Handling APIs")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Get All Events", description = "Get names of all events that are possible", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events Returned Successfully",
                    content = @Content(schema = @Schema(implementation = Events.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @GetMapping("/getAllEvents")
    public ResponseEntity<String> getAllEvents(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(eventService.getAllEvents(tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Enable Events", description = "Enable all events present in the string array", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event enabled",
                    content = @Content(schema = @Schema(implementation = Events.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping("/enableEvent")
    public ResponseEntity<String> enableEvents(@RequestParam String[] events, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            eventService.enableEvents(tenantId, events);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Disable Events", description = "Disable all events present in the string array", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event disabled",
                    content = @Content(schema = @Schema(implementation = Events.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping("/disableEvent")
    public ResponseEntity<String> disableEvents(@RequestParam String[] events, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            eventService.disableEvents(tenantId, events);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get All Enabled Events", description = "Get names of all events that are enabled", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enabled Events Returned Successfully",
                    content = @Content(schema = @Schema(implementation = Events.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @GetMapping("/getEnabledEvents")
    public ResponseEntity<List<String>> getEnabledEvents(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(eventService.getEnabledEvents(tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}












