package com.geofence.geo.service;

import com.geofence.geo.autocomplete.AutoCompleteResponse;
import com.geofence.geo.autocomplete.GoogleAutoCompleteResponse;
import com.geofence.geo.autocomplete.LocationIqAutoCompleteResponse;
import com.geofence.geo.model.autocompleteproviders.GenericAutocomplete;
import com.google.maps.errors.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.geofence.geo.constants.StringConstants.GOOGLE_AUTOCOMPLETE;
import static com.geofence.geo.constants.StringConstants.LOCIQ_AUTOCOMPLETE;

@Service
public class AutoCompleteService {
    public static final Logger autocompleteServiceLogger = LoggerFactory.getLogger("Autocomplete Service Logger");
    AutoCompleteResponse autoCompleteResponse;
    @Autowired
    TenantService tenantService;
    @Autowired
    GoogleAutoCompleteResponse googleAutoCompleteResponse;
    @Autowired
    LocationIqAutoCompleteResponse locationIqAutoCompleteResponse;

    public List<GenericAutocomplete> getAutoCompleteResponse(String tenantId, String locationQuery) throws InterruptedException, ApiException, IOException {
        autocompleteServiceLogger.info("Inside getAutoCompleteResponse");
        if (tenantService.getAutocompleteProviderById(tenantId).equals(GOOGLE_AUTOCOMPLETE)) {
            autoCompleteResponse = googleAutoCompleteResponse;
        } else if (tenantService.getAutocompleteProviderById(tenantId).equals(LOCIQ_AUTOCOMPLETE)) {
            autoCompleteResponse = locationIqAutoCompleteResponse;
        } else {
            return new ArrayList<>();
        }
        return autoCompleteResponse.getAutoCompleteResponse(locationQuery);
    }
}
