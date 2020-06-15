package com.geofence.geo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geofence.geo.events.Distance;
import com.geofence.geo.geocoding.GoogleGeocoding;
import com.geofence.geo.model.Polygon;
import com.geofence.geo.security.JwtTokenProvider;
import com.geofence.geo.service.GeofenceService;
import com.geofence.geo.service.RestService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.geofence.geo.constants.StringConstants.*;


@RestController
@RequestMapping("/geofence")
@Tag(name = "Geofence", description = "Geofencing APIs")
public class GeofenceController {
    @Autowired
    GeofenceService geofenceService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    RestService restService;
    @Autowired
    GoogleGeocoding googleGeocoding;
    @Autowired
    Distance distance;
    @Value("${locationiq.autocomplete.api_key}")
    private String locationIqApiKey;

    @Operation(summary = "Create Geofence", description = "Create a Geofence and set a name for the same.", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Geofence created",
                    content = @Content(schema = @Schema(implementation = Polygon.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Geofence already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/create")
    public ResponseEntity<String> createGeofence(@RequestBody String polyInfo, @RequestHeader("Authorization") String authorization) {
        String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
        Gson gson = new Gson();
        Polygon polygon;
        try {
            polygon = gson.fromJson(polyInfo, Polygon.class);
            polygon.setTenantId(tenantId);
            if (geofenceService.getByName(polygon.getName(), tenantId) != null)
                return new ResponseEntity<>("Geofence already exists for this tenant", HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(gson.toJson(geofenceService.create(polygon)), HttpStatus.CREATED);
        } catch (JsonParseException e) {
            return new ResponseEntity<>("Error reading polygon data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Polygon By ID", description = "Get a polygon using a polygon ID", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Polygon Retrieved Successfully",
                    content = @Content(schema = @Schema(implementation = Polygon.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getById")
    public ResponseEntity<Polygon> getGeofenceById(@RequestParam String id, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(geofenceService.getById(id, tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get Paginated List of Polygons", description = "Get a list of polygons for pagination", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Polygon List Retrieved Successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getPaginated")
    public ResponseEntity<List<Polygon>> getGeofencePaginated(@RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(defaultValue = "name") String sortBy,
                                                              @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            List<Polygon> geofenceList = geofenceService.getPaginated(pageNo, pageSize, sortBy, tenantId);
            return new ResponseEntity<>(geofenceList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get List of Saved Geofences", description = "Get the list of saved geofences in the database", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Polygon List Retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAllNames")
    public ResponseEntity<List<String>> getAllGeofence(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(geofenceService.getAllNames(tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Check if Records exist", description = "Returns if a polygon exists or not in the database", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "geofence not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/checkPoly")
    public ResponseEntity<Boolean> checkIfRecordExist(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            if (Boolean.TRUE.equals(geofenceService.checkDB(tenantId)))
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get geofence by name", description = "Returns a single geofence", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Polygon.class))),
            @ApiResponse(responseCode = "404", description = "geofence not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getByName")
    public ResponseEntity<Polygon> getGeofenceByName(@RequestParam String name, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(geofenceService.getByName(name, tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deletes all Polygons", description = "Deletes all the polygons present in the database.", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Polygon not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            geofenceService.deleteAll(tenantId);
            return new ResponseEntity<>("Successfully deleted all geofences", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a geofence by ID", description = "Delete a specified geofence using geofence ID", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "geofence not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteById(@RequestParam String id, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            geofenceService.deleteById(id, tenantId);
            return new ResponseEntity<>("Successfully deleted geofence", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not delete geofence: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Geofence", description = "Update the properties of an already saved geofence.", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geofence Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/update")
    public ResponseEntity<String> updateGeofence(@RequestParam String id,
                                                 @Valid @RequestBody String polygonInfo,
                                                 @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            Gson gson = new Gson();
            Polygon polygon = gson.fromJson(polygonInfo, Polygon.class);
            polygon.setTenantId(tenantId);
            return new ResponseEntity<>(geofenceService.update(id, polygon, tenantId).toString(), HttpStatus.OK);
        } catch (JsonParseException e) {
            return new ResponseEntity<>("Error reading polygon data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Geofence by location coordinates", description = "Get geofence coordinates from location coordinates", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geofence fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getGeofenceByCoordinates")
    public ResponseEntity<Map<String,String>> getGeofenceByCoordinates(@RequestParam(defaultValue = "10") String lat,
                                                           @RequestParam(defaultValue = "10") String lng) {
    	HashMap<String,String>responseMap=new HashMap<>();
        try {
            String reverseGeoCodingUrl = NOMINATIM_REVERSE_GEOCODING_URL + "?" + FORMAT_PARAM + "=" + STRING_JSON_V2 + "&" + LATITUDE_PARAM + "=" + lat +
                    "&" + LONGITUDE_PARAM + "=" + lng + "&" + ZOOM_PARAM + "=" + MAP_ZOOM_VALUE;
           responseMap= getGeofenceDetails(reverseGeoCodingUrl);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (IOException e) {
        	responseMap.put("Error",e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
        	responseMap.put("Error",e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Get Geofence by place id", description = "Get geofence coordinates from place id", tags = {"geofence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geofence fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getGeofenceByPlaceId")
    public ResponseEntity<Map<String,String>> getGeofenceByPlaceId(@RequestParam String placeId) {
    	HashMap<String,String>responseMap=new HashMap<>();
    	
        try {
            String[] placeDetails = placeId.split("__");
            if (placeDetails[0].equals("GO")) {
                placeId = placeDetails[1];
                double latitude = googleGeocoding.getCoordinatesFromPlaceId(placeId).lat;
                double longitude = googleGeocoding.getCoordinatesFromPlaceId(placeId).lng;
                String reverseGeoCodingUrl = NOMINATIM_REVERSE_GEOCODING_URL + "?" + FORMAT_PARAM + "=" + STRING_JSON_V2 +
                        "&" + LATITUDE_PARAM + "=" + latitude + "&" +
                        LONGITUDE_PARAM + "=" + longitude + "&" + ZOOM_PARAM + "=" + MAP_ZOOM_VALUE;
                responseMap= getGeofenceDetails(reverseGeoCodingUrl);
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                String osmId = placeDetails[1];
                String osmType = placeDetails[2];
                ObjectMapper objectMapper = new ObjectMapper();
                String url = LOCATION_IQ_URL + "?" + FORMAT_PARAM + "=" + STRING_JSON + "&" + OSM_ID_PARAM + "=" + osmId +
                        "&" + OSM_TYPE_PARAM + "=" + osmType + "&" + API_KEY_PARAM + "=" + locationIqApiKey;
                Map<?, ?> map = objectMapper.readValue(restService.getPostsPlainJSON(url), Map.class);
                LinkedHashMap<String, List<String>> geometryHashMap = (LinkedHashMap<String, List<String>>) map.get(GEOMETRY_ATTRIBUTE);
                String coordinates = String.valueOf(geometryHashMap.get(COORDINATES_ATTRIBUTE));
                String[] coords = coordinates.split(",");
                String reverseGeoCodingUrl = NOMINATIM_REVERSE_GEOCODING_URL + "?" + FORMAT_PARAM + "=" + STRING_JSON_V2 +
                        "&" + LATITUDE_PARAM + "=" + coords[0].substring(1) + "&" + LONGITUDE_PARAM + "=" +
                        coords[1].substring(1, coords[1].length() - 1) + "&" + ZOOM_PARAM + "=" + MAP_ZOOM_VALUE;
                responseMap= getGeofenceDetails(reverseGeoCodingUrl);
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
           
            }
        } catch (Exception e) {
            responseMap.put("Error",e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HashMap<String,String> getGeofenceDetails (String reverseGeoCodingUrl) throws com.fasterxml.jackson.core.JsonProcessingException {
    	HashMap<String,String>responseMap=new HashMap<>();
    	ObjectMapper objectMapper = new ObjectMapper();
        Map<?, ?> reverseGeoCodingMap = objectMapper.readValue(restService.getPostsPlainJSON(reverseGeoCodingUrl), Map.class);
        String displayName = (String) reverseGeoCodingMap.get(DISPLAY_NAME);
        responseMap.put("displayName", displayName);
        String getCoordinatesUrl = NOMINATIM_GEOCODING_URL + "?" + NOMINATIM_QUERY_PARAM + "=" + displayName +
                "&" + POLYGON_GEO_JSON_PARAM + "=1" + "&" + FORMAT_PARAM + "=" + STRING_JSON;
        List<HashMap> dataAsMap = objectMapper.readValue(restService.getPostsPlainJSON(getCoordinatesUrl), List.class);
        LinkedHashMap<String, List<String>> coordinatesHashMap = (LinkedHashMap<String, List<String>>) dataAsMap.get(0).get(GEOJSON);
       // return new ResponseEntity<>(coordinatesHashMap.get(COORDINATES_ATTRIBUTE).toString(), HttpStatus.OK);
        responseMap.put("coordinates", coordinatesHashMap.get(COORDINATES_ATTRIBUTE).toString());
        return responseMap;
    }

}
