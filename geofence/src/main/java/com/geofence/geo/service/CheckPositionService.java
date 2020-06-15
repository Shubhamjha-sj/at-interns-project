package com.geofence.geo.service;

import com.geofence.geo.events.Distance;
import com.geofence.geo.events.Events;
import com.geofence.geo.events.GeoUtil;
import com.geofence.geo.model.Marker;
import com.geofence.geo.model.Polygon;
import com.geofence.geo.repository.AssetRepository;
import com.geofence.geo.repository.TenantRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.geofence.geo.constants.StringConstants.*;

@Service
public class CheckPositionService {
    public static final Logger checkPositionLogger = LoggerFactory.getLogger("CheckPosition Service Logger");
    @Autowired
    Distance distanceJava;
    @Autowired
    TenantRepository tenantRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    private GeofenceService geofenceService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String savePosition(String tenantId, String markerInfo) {
        checkPositionLogger.info("Inside savePosition");
        checkPositionLogger.info(tenantId);
        Gson gson = new Gson();
        Marker marker = gson.fromJson(markerInfo, Marker.class);
        marker.setGeofenceEnterTime(0L);
        marker.setDwellTime(0L);
        marker.setDistance((double) 0);
        marker.setCurrentState("");
        marker.setDistance((double) 0);
        assetRepository.save(marker);
        return gson.toJson(marker);
    }

    public String getPosition(String tenantId, String markerInfo, Integer proximity) {
        checkPositionLogger.info("Inside getPosition");
        String currPosition;
        Gson gson = new Gson();
        Marker marker = gson.fromJson(markerInfo, Marker.class);
        Marker dbMarker = assetRepository.getByMarkerID(marker.getMarkerID());
        String geofenceID = marker.getGeofenceID();
        String prevPosition = marker.getCurrentState();
        Polygon polygon = geofenceService.getById(geofenceID, tenantId);
        int distance = (int) Math.round(distanceJava.getDistanceBetweenMarkerandGeofence(marker.getMarkerPosition(), polygon.getCoordinates()));
        marker.setDistance((double) Math.round(distanceJava.getDistanceBetweenMarkerandGeofence(marker.getMarkerPosition(), polygon.getCoordinates())));
        int polyProximity;
        if (proximity == -1)
            polyProximity = polygon.getProximity();
        else
            polyProximity = proximity;
        if (GeoUtil.contains(polygon.getCoordinates(), marker.getMarkerPosition())) {
            currPosition = INSIDE;
        } else {
            currPosition = OUTSIDE;
        }
        marker.setCurrentState(currPosition);
        dbMarker.setDistance((double) distance);
        dbMarker.setMarkerPosition(marker.getMarkerPosition());
        dbMarker.setCurrentState(marker.getCurrentState());
        return eventHandling(currPosition, gson, marker, dbMarker, prevPosition, distance, polyProximity);
    }

    public String getAllMarkerStates(String tenantId, String markersInfo) {
        checkPositionLogger.info("Inside getAllMarkers");
        Gson gson = new Gson();
        Marker[] markers = gson.fromJson(markersInfo, Marker[].class);
        List<Marker> markerFinal = new ArrayList<>();
        for (Marker n : markers) {
            Polygon polygon = geofenceService.getById(n.getGeofenceID(), tenantId);
            String currPosition;
            if (GeoUtil.contains(polygon.getCoordinates(), n.getMarkerPosition())) {
                currPosition = INSIDE;
            } else {
                currPosition = OUTSIDE;
            }
            n.setCurrentState(currPosition);
            markerFinal.add(n);
        }
        return gson.toJson(markerFinal);
    }

    public String getSelectiveMarkers(String tenantId, String markersInfo, String selector) {
        checkPositionLogger.info("Inside getSelectiveMarkers");
        Gson gson = new Gson();
        Marker[] markers = gson.fromJson(markersInfo, Marker[].class);

        List<Marker> markerList = new ArrayList<>();
        for (Marker marker : markers) {
            Polygon polygon = geofenceService.getById(marker.getGeofenceID(), tenantId);

            if (GeoUtil.contains(polygon.getCoordinates(), marker.getMarkerPosition())) {
                marker.setCurrentState(INSIDE);
            } else {
                marker.setCurrentState(OUTSIDE);
            }

            markerList.add(marker);
        }

        List<Marker> markerFinal;
        if (selector.equals(INSIDE)) {
            markerFinal = markerList.stream().filter(n -> n.getCurrentState().equals(INSIDE)).collect(Collectors.toList());
        } else
            markerFinal = markerList.stream().filter(n -> n.getCurrentState().equals(OUTSIDE)).collect(Collectors.toList());
        return gson.toJson(markerFinal);
    }

    private String eventHandling(String currPosition, Gson gson, Marker marker, Marker dbMarker, String prevPosition, int distance, int polyProximity) {
        checkPositionLogger.info("Inside eventHandling");
        String polyInf = gson.toJson(marker);
        if (!currPosition.equals(prevPosition) && distance <= polyProximity) {

            if (currPosition.equals("Inside")) {
                if (Events.Event.ENTRY.getState())
                    rabbitTemplate.convertAndSend(MQ_EXCHANGE, QUEUE_ENTER, String.valueOf(polyInf));
                else
                    checkPositionLogger.info("Not listening to enter event");
            } else {
                if (Events.Event.EXIT.getState())
                    rabbitTemplate.convertAndSend(MQ_EXCHANGE, QUEUE_EXIT, String.valueOf(polyInf));
                else {
                    checkPositionLogger.info("Not listening to exit event");
                }
            }

        }
        assetRepository.save(dbMarker);

        return "{\"status\":\"" + currPosition + "\",\"proximity\":" + polyProximity + ",\"distance\":" + distance + "}";
    }

}
