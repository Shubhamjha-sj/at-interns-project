package com.geofence.geo.controller;

import com.geofence.geo.model.Tenant;
import com.geofence.geo.payload.JWTLoginSucessReponse;
import com.geofence.geo.payload.LoginRequest;
import com.geofence.geo.security.JwtTokenProvider;
import com.geofence.geo.service.MapValidationErrorService;
import com.geofence.geo.service.TenantService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.geofence.geo.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@Tag(name = "Tenant", description = "Tenant APIs")
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    TenantService tenantService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Get Tenant Name By ID", description = "Get a Tenant name using a Tenant ID", tags = {"Tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant Retrieved Successfully",
                    content = @Content(schema = @Schema(implementation = Tenant.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @GetMapping("/getTenantName")
    public ResponseEntity<String> getTenantName(@RequestParam String id) {
        try {
            return new ResponseEntity<>(tenantService.getTenant(id).getName(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutocompleteProvider")
    public ResponseEntity<String> getAutocompleteProvider(@RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(tenantService.getAutocompleteProviderById(tenantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a Tenant by ID", description = "Delete a specified Tenant using Tenant ID", tags = {"Tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")})
    @DeleteMapping("/deleteTenant")
    public ResponseEntity<String> deleteTenantById(@RequestParam String id) {
        try {
            tenantService.deleteTenantById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deletes all Tenants", description = "Deletes all the Tenants present in the database.", tags = {"Tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")})
    @DeleteMapping("/deleteAllTenants")
    public ResponseEntity<String> deleteAllTenants() {
        try {
            tenantService.deleteAllTenant();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Tenant Name", description = "Update the name of an already saved tenant.", tags = {"tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PutMapping("/updateTenantName")
    public ResponseEntity<String> updateTenantNameById(@RequestBody String name, @RequestParam String id) {
        try {
            tenantService.updateTenant(id, name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Update AutoComplete Provider", description = "Update the Autocomplete provider of an already saved tenant.", tags = {"tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})

    @PutMapping("/updateAutocompleteProvider")
    public ResponseEntity<String> updateAutocompleteProvider(@RequestParam String selectedProvider, @RequestHeader("Authorization") String authorization) {
        try {
            String tenantId = jwtTokenProvider.getTenantIdFromJWTHeader(authorization);
            return new ResponseEntity<>(tenantService.updateAutocompleteProvider(tenantId, selectedProvider), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Login Tenant", description = "Login of an already registered tenant", tags = {"tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant Logged In"),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getName(), loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }

    @Operation(summary = " Register a Tenant", description = "Register a new tenant", tags = {"tenant"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant Logged In"),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody String tenantInfo, BindingResult result) {
        Gson gson = new Gson();
        Tenant tenant = gson.fromJson(tenantInfo, Tenant.class);

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        Tenant newTenant = tenantService.createTenant(tenant);
        return new ResponseEntity<>(newTenant, HttpStatus.CREATED);
    }

}
