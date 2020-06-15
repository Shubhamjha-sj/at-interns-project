package com.geofence.geo.repository;

import com.geofence.geo.model.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantRepository extends MongoRepository<Tenant, String> {
    Tenant getById(String id);

    void deleteTenantById(String id);

    void deleteAll();

    Tenant findByName(String name);

    Tenant findByUsername(String name);
}
