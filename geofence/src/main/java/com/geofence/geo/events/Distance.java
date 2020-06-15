package com.geofence.geo.events;

import com.geofence.geo.model.Coordinates;
import com.geofence.geo.repository.PolygonRepository;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class Distance {
    @Autowired
   public  PolygonRepository polygonRepository;
    public  Double getDistanceBetweenMarkerandGeofence(Coordinates coordinates,Coordinates[]polygonCoordinates){
        List<LatLng>list=getLatLngListFromCoordinates(polygonCoordinates);
        LatLng latLng=new LatLng(coordinates.getLat(),coordinates.getLng());
        return SphericalUtil.computeDistanceBetween(latLng, findNearestPoint(latLng,list));
    }
    private   List<LatLng> getLatLngListFromCoordinates(Coordinates[]coordinates){
        List<LatLng>list=new ArrayList<>();
        for(int i=0;i<coordinates.length;i++){
            list.add(new LatLng(coordinates[i].getLat(),coordinates[i].getLng()));}
        return list;

    }

    private LatLng findNearestPoint(LatLng test, List<LatLng> target) {
        double distance = -1;
        LatLng minimumDistancePoint = test;

        if (test == null || target == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < target.size(); i++) {
            LatLng point = target.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= target.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(test, point, target.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(test, point, target.get(segmentPoint));
            }
        }

        return minimumDistancePoint;
    }
    private LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.lat);
        final double s0lng = Math.toRadians(p.lng);
        final double s1lat = Math.toRadians(start.lat);
        final double s1lng = Math.toRadians(start.lng);
        final double s2lat = Math.toRadians(end.lat);
        final double s2lng = Math.toRadians(end.lng);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.lat + (u * (end.lat - start.lat)),
                start.lng + (u * (end.lng - start.lng)));


    }


}
