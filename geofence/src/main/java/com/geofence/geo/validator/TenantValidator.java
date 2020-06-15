package com.geofence.geo.validator;

import com.geofence.geo.model.Tenant;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TenantValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Tenant.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        // Yet to implement confirm password.


    }
}
