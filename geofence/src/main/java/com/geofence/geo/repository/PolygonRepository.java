package com.geofence.geo.repository;

import com.geofence.geo.model.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolygonRepository extends MongoRepository<Polygon, String> {
    Long deletePolygonByIdAndTenantId(String id,String tenantId);
    Polygon findDistinctById(String id,String tenantId);
    Polygon findDistinctByNameAndTenantId(String name,String tenantId);
    Page<Polygon> findByTenantId(String tenantId, Pageable pageable);
    List<Polygon> findByTenantId(String tenantId);


    List<Polygon>deleteAllByTenantId(String tenantId);



}
