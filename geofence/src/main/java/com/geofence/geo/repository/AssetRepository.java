package com.geofence.geo.repository;

import com.geofence.geo.model.Marker;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AssetRepository extends MongoRepository<Marker, String> {
    Marker getByMarkerID(String markerId);

}
