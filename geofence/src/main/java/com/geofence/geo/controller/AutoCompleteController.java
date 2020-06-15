package com.geofence.geo.controller;

import com.geofence.geo.model.autocompleteproviders.GenericAutocomplete;
import com.geofence.geo.security.JwtTokenProvider;
import com.geofence.geo.service.AutoCompleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/autoComplete")
public class AutoCompleteController {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AutoCompleteService autoCompleteService;

    @Operation(summary = "Get Autocomplete Search response",
            description = "Get Autocomplete Search responses based on the autocomplete provider selected by the Tenant",
            tags = {"Autocomplete"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autocomplete Response retrieved",
                    content = @Content(schema = @Schema(implementation = GenericAutocomplete[].class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/searchPlaces")
    public ResponseEntity<List<GenericAutocomplete>> getAutocompleteResponse(@RequestParam String locationQuery,
                                                                             @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            if (locationQuery.length() >= 5) {
                List<GenericAutocomplete> autoCompleteResponse = autoCompleteService.getAutoCompleteResponse(tenantId, locationQuery);
                if (autoCompleteResponse.isEmpty()) {
                    return new ResponseEntity<>(autoCompleteResponse, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(autoCompleteResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
