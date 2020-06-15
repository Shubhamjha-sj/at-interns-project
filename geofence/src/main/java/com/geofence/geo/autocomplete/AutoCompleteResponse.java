package com.geofence.geo.autocomplete;

import com.geofence.geo.model.autocompleteproviders.GenericAutocomplete;
import com.google.maps.errors.ApiException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface AutoCompleteResponse {
    List<GenericAutocomplete> getAutoCompleteResponse(String locationQuery) throws InterruptedException, ApiException, IOException;
}
