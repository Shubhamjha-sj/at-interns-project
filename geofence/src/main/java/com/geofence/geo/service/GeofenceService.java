package com.geofence.geo.service;

import com.geofence.geo.model.Polygon;
import com.geofence.geo.repository.PolygonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeofenceService {
    public static final Logger geofenceServiceLogger = LoggerFactory.getLogger("Geofence Service Logger");

    @Autowired
    private PolygonRepository polygonRepository;

    public Polygon create(Polygon polygon) {
        geofenceServiceLogger.info("Inside create");
        return polygonRepository.save(new Polygon(polygon));
    }

    public Polygon getById(String geofenceId, String tenantId) {
        geofenceServiceLogger.info("Inside getById");
        return polygonRepository.findDistinctById(geofenceId, tenantId);
    }

    public List<Polygon> getPaginated(Integer pageNo, Integer pageSize, String sortBy, String tenantId) {
        geofenceServiceLogger.info("Inside getPaginated");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Polygon> pagedResult = polygonRepository.findByTenantId(tenantId, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    public Polygon getByName(String name, String tenantId) {
        geofenceServiceLogger.info("Inside getByName");
        return polygonRepository.findDistinctByNameAndTenantId(name, tenantId);
    }

    public List<String> getAllNames(String tenantId) {
        geofenceServiceLogger.info("Inside getAllNames");
        List<Polygon> polygonList = polygonRepository.findByTenantId(tenantId);
        List<String> polyNameList = new ArrayList<>();
        for (Polygon polygon : polygonList) {
            polyNameList.add(polygon.getName());
        }
        return polyNameList;
    }

    public Polygon update(String geofenceId, Polygon polygon, String tenantId) {
        geofenceServiceLogger.info("Inside update");
        Polygon updatedPolygon = polygonRepository.findDistinctById(geofenceId, tenantId);
        updatedPolygon.setCoordinates(polygon.getCoordinates());
        updatedPolygon.setName(polygon.getName());
        return polygonRepository.save(updatedPolygon);
    }

    public boolean deleteById(String geofenceId, String tenantId) {
        geofenceServiceLogger.info("Inside deleteById");
        return polygonRepository.deletePolygonByIdAndTenantId(geofenceId, tenantId) > 0;
    }

    public void deleteAll(String tenantId) {
        geofenceServiceLogger.info("Inside deleteAll");
        polygonRepository.deleteAllByTenantId(tenantId);
    }

    public Boolean checkDB(String tenantId) {
        geofenceServiceLogger.info("Inside checkDB");
        return !polygonRepository.findByTenantId(tenantId).isEmpty();
    }
}
