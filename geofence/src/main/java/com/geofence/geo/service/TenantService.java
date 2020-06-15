package com.geofence.geo.service;

import com.geofence.geo.exception.UsernameAlreadyExistsException;
import com.geofence.geo.model.Tenant;
import com.geofence.geo.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class TenantService {
    public static final Logger tenantServiceLogger = LoggerFactory.getLogger("Tenant Service Logger");

    @Autowired
    TenantRepository tenantRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Tenant createTenant(Tenant tenant) {
        tenantServiceLogger.info("Inside createTenant");
        tenant.setPassword(bCryptPasswordEncoder.encode(tenant.getPassword()));
        if (tenantRepository.findByName(tenant.getName()) == null)
            return tenantRepository.save(tenant);
        else
            throw new UsernameAlreadyExistsException("Username " + tenant.getName() + " already exists");
    }

    public Tenant getTenant(String id) {
        tenantServiceLogger.info("Inside getTenant");
        return tenantRepository.getById(id);
    }

    public void deleteTenantById(String id) {
        tenantServiceLogger.info("Inside deleteTenantById");
        tenantRepository.deleteTenantById(id);
    }

    public void deleteAllTenant() {
        tenantServiceLogger.info("Inside deleteAllTenant");
        tenantRepository.deleteAll();
    }

    public Tenant updateTenant(String id, String name) {
        tenantServiceLogger.info("Inside updateTenant");
        Tenant updatedTenant = tenantRepository.getById(id);
        updatedTenant.setName(name);
        return tenantRepository.save(updatedTenant);
    }

    public String getAutocompleteProviderById(String id) {
        tenantServiceLogger.info("Inside getAutocompleteProviderById");
        Tenant tenant = tenantRepository.getById(id);
        return tenant.getAutocompleteProvider();
    }

    public String updateAutocompleteProvider(String id, String selectedProvider) {
        tenantServiceLogger.info("Inside updateAutocompleteProvider");
        Tenant tenant = tenantRepository.getById(id);
        tenant.setAutocompleteProvider(selectedProvider);
        tenantRepository.save(tenant);
        return tenant.getAutocompleteProvider();
    }

}
