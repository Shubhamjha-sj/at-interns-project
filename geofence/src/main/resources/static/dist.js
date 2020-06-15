function bdccGeo(lat, lon) {
    let theta = (lon * Math.PI / 180.0);
    let rlat = bdccGeoGeocentricLatitude(lat * Math.PI / 180.0);
    let c = Math.cos(rlat);
    this.x = c * Math.cos(theta);
    this.y = c * Math.sin(theta);
    this.z = Math.sin(rlat);
}

bdccGeo.prototype = new bdccGeo();

// internal helper functions =========================================

// Convert from geographic to geocentric latitude (radians).
function bdccGeoGeocentricLatitude(geographicLatitude) {
    let flattening = 1.0 / 298.257223563;//WGS84
    let f = (1.0 - flattening) * (1.0 - flattening);
    return Math.atan((Math.tan(geographicLatitude) * f));
}

// Returns the two antipodal points of intersection of two great
// circles defined by the arcs geo1 to geo2 and
// geo3 to geo4. Returns a point as a Geo, use .antipode to get the other point
function bdccGeoGetIntersection(geo1, geo2, geo3, geo4) {
    let geoCross1 = geo1.crossNormalize(geo2);
    let geoCross2 = geo3.crossNormalize(geo4);
    return geoCross1.crossNormalize(geoCross2);
}

//from Radians to Meters
function bdccGeoRadiansToMeters(rad) {
    return rad * 6378137.0; // WGS84 Equatorial Radius in Meters
}

//from Meters to Radians
function bdccGeoMetersToRadians(m) {
    return m / 6378137.0; // WGS84 Equatorial Radius in Meters
}

// properties =================================================


bdccGeo.prototype.getLatitudeRadians = function () {
    return (bdccGeoGeographicLatitude(Math.atan2(this.z,
        Math.sqrt((this.x * this.x) + (this.y * this.y)))));
};

bdccGeo.prototype.getLongitudeRadians = function () {
    return (Math.atan2(this.y, this.x));
};

bdccGeo.prototype.getLatitude = function () {
    return this.getLatitudeRadians() * 180.0 / Math.PI;
};

bdccGeo.prototype.getLongitude = function () {
    return this.getLongitudeRadians() * 180.0 / Math.PI;
};

// Methods =================================================

//Maths
bdccGeo.prototype.dot = function (b) {
    return ((this.x * b.x) + (this.y * b.y) + (this.z * b.z));
};

//More Maths
bdccGeo.prototype.crossLength = function (b) {
    let x = (this.y * b.z) - (this.z * b.y);
    let y = (this.z * b.x) - (this.x * b.z);
    let z = (this.x * b.y) - (this.y * b.x);
    return Math.sqrt((x * x) + (y * y) + (z * z));
};

//More Maths
bdccGeo.prototype.scale = function (s) {
    let r = new bdccGeo(0, 0);
    r.x = this.x * s;
    r.y = this.y * s;
    r.z = this.z * s;
    return r;
};

// More Maths
bdccGeo.prototype.crossNormalize = function (b) {
    let x = (this.y * b.z) - (this.z * b.y);
    let y = (this.z * b.x) - (this.x * b.z);
    let z = (this.x * b.y) - (this.y * b.x);
    let L = Math.sqrt((x * x) + (y * y) + (z * z));
    let r = new bdccGeo(0, 0);
    r.x = x / L;
    r.y = y / L;
    r.z = z / L;
    return r;
};

// point on opposite side of the world to this point
bdccGeo.prototype.antipode = function () {
    return this.scale(-1.0);
};


//distance in radians from this point to point v2
bdccGeo.prototype.distance = function (v2) {
    return Math.atan2(v2.crossLength(this), v2.dot(this));
};

//returns in meters the minimum of the perpendicular distance of this point from the line segment geo1-geo2
//and the distance from this point to the line segment ends in geo1 and geo2
bdccGeo.prototype.distanceToLineSegMtrs = function (geo1, geo2) {

    //point on unit sphere above origin and normal to plane of geo1,geo2
    //could be either side of the plane
    let p2 = geo1.crossNormalize(geo2);

    // intersection of GC normal to geo1/geo2 passing through p with GC geo1/geo2
    let ip = bdccGeoGetIntersection(geo1, geo2, this, p2);

    //need to check that ip or its antipode is between p1 and p2
    let d = geo1.distance(geo2);
    let d1p = geo1.distance(ip);
    let d2p = geo2.distance(ip);
    //window.status = d + ", " + d1p + ", " + d2p;
    if ((d >= d1p) && (d >= d2p))
        return bdccGeoRadiansToMeters(this.distance(ip));
    else {
        ip = ip.antipode();
        d1p = geo1.distance(ip);
        d2p = geo2.distance(ip);
    }
    if ((d >= d1p) && (d >= d2p))
        return bdccGeoRadiansToMeters(this.distance(ip));
    else
        return bdccGeoRadiansToMeters(Math.min(geo1.distance(this), geo2.distance(this)));
};

// distance in meters from GLatLng point to GPolyline or GPolygon poly
function bdccGeoDistanceToPolyMtrs(poly, point) {
    let d = 999999999;
    let i;
    let p = new bdccGeo(point.lat(), point.lng());

    let p1;
    let l1;
    let p2;
    let l2;
    let dp;

    for (i = 0; i < (poly.getPath().getLength() - 1); i++) {
        p1 = poly.getPath().getAt(i);
        l1 = new bdccGeo(p1.lat(), p1.lng());
        p2 = poly.getPath().getAt(i + 1);
        l2 = new bdccGeo(p2.lat(), p2.lng());
        dp = p.distanceToLineSegMtrs(l1, l2);
        if (dp < d)
            d = dp;
    }
    p1 = poly.getPath().getAt(0);
    l1 = new bdccGeo(p1.lat(), p1.lng());
    p2 = poly.getPath().getAt(poly.getPath().getLength() - 1);
    l2 = new bdccGeo(p2.lat(), p2.lng());
    dp = p.distanceToLineSegMtrs(l1, l2);
    if (dp < d)
        d = dp;
    return d;
}

// get a new GLatLng distanceMeters away on the compass bearing azimuthDegrees
// from the GLatLng point - accurate to better than 200m in 140km (20m in 14km) in the UK

function bdccGeoPointAtRangeAndBearing(point, distanceMeters, azimuthDegrees) {
    let latr = point.lat() * Math.PI / 180.0;
    let lonr = point.lng() * Math.PI / 180.0;

    let coslat = Math.cos(latr);
    let sinlat = Math.sin(latr);
    let az = azimuthDegrees * Math.PI / 180.0;
    let cosaz = Math.cos(az);
    let sinaz = Math.sin(az);
    let dr = distanceMeters / 6378137.0; // distance in radians using WGS84 Equatorial Radius
    let sind = Math.sin(dr);
    let cosd = Math.cos(dr);

    return new google.maps.LatLng(Math.asin((sinlat * cosd) + (coslat * sind * cosaz)) * 180.0 / Math.PI,
        (Math.atan2((sind * sinaz), (coslat * cosd) - (sinlat * sind * cosaz)) + lonr) * 180.0 / Math.PI);
}
