/**
 * Copyright © 2016-2020 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.geofence.geo.events;

import com.geofence.geo.model.Coordinates;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.SpatialRelation;

public class GeoUtil {

    private static final SpatialContext distCtx = SpatialContext.GEO;
    private static final JtsSpatialContext jtsCtx;

    static {
        JtsSpatialContextFactory factory = new JtsSpatialContextFactory();
        factory.normWrapLongitude = true;
        jtsCtx = factory.newSpatialContext();
    }

    private GeoUtil() {
    }

    public static synchronized double distance(com.geofence.geo.model.Coordinates x, Coordinates y, RangeUnit unit) {
        Point xLL = distCtx.getShapeFactory().pointXY(x.getLng(), x.getLat());
        Point yLL = distCtx.getShapeFactory().pointXY(y.getLng(), y.getLat());
        return unit.fromKm(distCtx.getDistCalc().distance(xLL, yLL) * DistanceUtils.DEG_TO_KM);
    }

    public static synchronized boolean contains(Coordinates[] polygonArray, com.geofence.geo.model.Coordinates coordinates) {
        ShapeFactory.PolygonBuilder polygonBuilder = jtsCtx.getShapeFactory().polygon();
        boolean first = true;
        double firstLat = 0.0;
        double firstLng = 0.0;
        for (Coordinates coordinates1 : polygonArray) {

            if (first) {
                firstLat = coordinates1.getLat();
                firstLng = coordinates1.getLng();
                first = false;
            }
            polygonBuilder.pointXY(jtsCtx.getShapeFactory().normX(coordinates1.getLng()), jtsCtx.getShapeFactory().normY(coordinates1.getLat()));
        }
        polygonBuilder.pointXY(jtsCtx.getShapeFactory().normX(firstLng), jtsCtx.getShapeFactory().normY(firstLat));
        Shape shape = polygonBuilder.buildOrRect();
        Point point = jtsCtx.makePoint(coordinates.getLng(), coordinates.getLat());
        return shape.relate(point).equals(SpatialRelation.CONTAINS);
    }
}
