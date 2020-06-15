package com.geofence.geo.service;

import com.geofence.geo.model.Tenant;
import com.geofence.geo.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantDetailsService implements UserDetailsService {
    public static final Logger tenantDetailsServiceLogger = LoggerFactory.getLogger("Tenant Details Service Logger");

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        tenantDetailsServiceLogger.info("Inside loadUserByUsername");
        Tenant tenant = tenantRepository.findByName(name);
        if (tenant == null) throw new UsernameNotFoundException("User not found");
        return tenant;
    }


    @Transactional
    public Tenant loadUserById(String id) {
        tenantDetailsServiceLogger.info("Inside loadUserById");
        Tenant tenant = tenantRepository.getById(id);
        if (tenant == null) throw new UsernameNotFoundException("User not found");
        return tenant;

    }
}
