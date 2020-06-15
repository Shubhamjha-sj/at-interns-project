let proximityThreshold = 100.0;
let mapLoc;
let today = new Date();
let time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
let geofenceDetails;
let jwt;
var markers = [];

window.onload = function() {
    let requestOptions = {
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };

    const root = document.querySelector('.autocomplete');
    document.addEventListener('click', event => {
        if (!root.contains(event.target)) {
            dropdown.classList.remove('is-active');
        }
    });
    root.innerHTML = `
  <input class="input" />
  <div class="dropdown">
    <div class="dropdown-menu">
      <div class="dropdown-content results"></div>
    </div>
  </div>
`;
    const input = document.querySelector('input');
    const dropdown = document.querySelector('.dropdown');
    const resultsWrapper = document.querySelector('.results');

    input.addEventListener('input', (event) => {

        fetch("autoComplete/searchPlaces?locationQuery=" + event.target.value, requestOptions)
            .then(response => {
                return response.json();
            })
            .then(data => {
                if (event.target.value.length >= 5) {
                    var placeArray = data;
                    if (!placeArray.length) {
                        dropdown.classList.remove('is-active');
                        return;
                    }

                    resultsWrapper.innerHTML = '';
                    dropdown.classList.add('is-active');

                    for (let place of placeArray) {
                        const option = document.createElement('a');

                        option.classList.add('dropdown-item');
                        option.innerHTML = place.displayName;
                        option.addEventListener('click', () => {
                            dropdown.classList.remove('is-active');
                            input.value = place.displayName;
                            let autoSearch = new google.maps.LatLng({lat: place.latitude, lng: place.longitude});

                            OSMCallback(autoSearch);
                        })

                        resultsWrapper.appendChild(option);


                    }
                }

            });

    })
};


function updateEvents() {
    var enabledEvents = [];
    var disabledEvents = [];

    if (document.getElementById('ENTRY').checked)
        enabledEvents.push("ENTRY");
    else
        disabledEvents.push("ENTRY");
    if (document.getElementById('EXIT').checked)
        enabledEvents.push("EXIT");
    else
        disabledEvents.push("EXIT");
    if (document.getElementById('PROXIMITY').checked)
        enabledEvents.push("PROXIMITY");
    else
        disabledEvents.push("PROXIMITY");
    let requestOptions = {
        method: 'POST',
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };
    if (enabledEvents.length !== 0) {

        fetch("event/enableEvent?events=" + enabledEvents.join(), requestOptions)
            .then(response => {
                return response.text()
            })
            .then(data => {

            });
    }
    if (disabledEvents.length !== 0) {
        fetch("event/disableEvent?events=" + disabledEvents.join(), requestOptions)
            .then(response => {
                return response.text()
            })
            .then(data => {

            });
    }


}

function OSMCallback(autoSearch) {
    document.getElementById('exampleMarkerLocation').value = autoSearch.lat() + ", " + autoSearch.lng();
    document.getElementById('exampleInputLocation').value = autoSearch.lat() + ", " + autoSearch.lng();
    fetch('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + autoSearch.lat() + '&lon=' + autoSearch.lng() + '&zoom=18')
        .then(response => {
            return response.json()
        })
        .then(data => {
            let ogLocationAddress = data.display_name;
            OSMCallback2(ogLocationAddress, autoSearch);
            return ogLocationAddress;
        });
}

function OSMCallback2(locationAddress, autoSearch) {
    fetch(`https://nominatim.openstreetmap.org/search.php?q=${locationAddress}&polygon_geojson=1&format=json`)
        .then(resp => resp.json())
        .then(data => {
            try {
                let coordsOSM = data[0].geojson.coordinates[0];
                plotOnPointOSM(coordsOSM, autoSearch);
            } catch (e) {
                plotOnPoint(autoSearch);
            }
        });
}

function setProximityThreshold() {
    let threshold = document.getElementById("proximityThresholdInput").value;
    if (threshold === '') {
        proximityThreshold = -1;
    } else {
        proximityThreshold = threshold;
    }
}

function createDistanceWindow(map, marker, distance, poly, position) {

    if (marker.prevWindow != null) {
        marker.prevWindow.close();
    }
    if (position === "Inside") {
        marker.setIcon({url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"});
        let contentStringInside = Math.floor(distance) + "m <b>Inside</b>";
        let infoWindow = new google.maps.InfoWindow({
            content: contentStringInside
        });
        infoWindow.open(map, marker);
        marker.prevWindow = infoWindow;

    } else {
        marker.setIcon({url: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'});
        let contentStringOutside = Math.floor(distance) + "m <b>Outside</b>";
        let infoWindow = new google.maps.InfoWindow({
            content: contentStringOutside
        });
        infoWindow.open(map, marker);
        marker.prevWindow = infoWindow;
    }
}

function eventHandler(map, geoAT, marker) {
    let proximityParam = "";
    if (proximityThreshold != null)
        proximityParam = "?proximity=" + proximityThreshold;
    if (geofenceDetails != null) {
        let jsonData = {
            "markerID": marker.markerID,
            "currentState": marker.currPos,
            "timeStamp": time,
            "markerPosition": marker.getPosition(),
            "geofenceID": geofenceDetails.id,
            "geofenceName": geofenceDetails.name
        };

        let requestOptions = {
            method: 'POST',
            redirect: 'follow',
            body: JSON.stringify(jsonData),
            headers: {
                Authorization: jwt
            }
        };

        fetch("position/checkPosition" + proximityParam, requestOptions)
            .then(response => response.text())
            .then(result => {
                let responseObject = JSON.parse(result);
                proximityThreshold = responseObject.proximity;
                var distance = responseObject.distance;
                if (proximityThreshold === -1 || distance <= proximityThreshold) {
                    createDistanceWindow(map, marker, distance, geoAT, responseObject.status);
                    if (google.maps.geometry.poly.isLocationOnEdge(marker.getPosition(), geoAT, 10e-7)) {
                        marker.setIcon({url: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"});
                        document.getElementById('result').innerHTML = "On the Geofence";
                    } else {
                        if (responseObject.status === "Inside") {
                            marker.currPos = "Inside";
                            if (marker.currPos !== marker.prevPos) {
                                marker.prevPos = marker.currPos;
                            }
                            document.getElementById('result').innerHTML = "Inside the Polygon!!";
                        } else {
                            marker.currPos = "Outside";
                            if (marker.currPos !== marker.prevPos) {
                                marker.prevPos = marker.currPos;
                            }
                            document.getElementById('result').innerHTML = "Outside the Polygon!!";
                        }
                    }
                } else {
                    marker.prevWindow.close();
                }
            })
            .catch(error => console.log('error', error));
    }
}

function checkMarker() {
    let map = mapLoc.map;
    let geoAT = mapLoc.geoAT;
    /*  if (prevMark != null) {
           prevMark.setMap(null);
       }*/
    let locationMarker = geoAT.getPath().getArray()[0];
    const marker = new google.maps.Marker({position: locationMarker, map: map, draggable: true});
    marker.setIcon({url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"});
    marker.setMap(map);
    marker.prevPos = null;
    markers.push(marker);
    // var markerCluster = new MarkerClusterer(map, markers);
    document.getElementById("exampleMarkerLocation").innerHTML = marker.getPosition();
    let jsonData;
    if (geofenceDetails != null) {
        jsonData = {
            "currentState": marker.currPos,
            "timeStamp": time,
            "markerPosition": marker.getPosition(),
            "geofenceID": geofenceDetails.id,
            "geofenceName": geofenceDetails.name
        };
    } else {
        jsonData = {
            "currentState": marker.currPos,
            "timeStamp": time,
            "markerPosition": marker.getPosition(),
        };
    }
    let requestOptions = {
        method: 'POST',
        redirect: 'follow',
        body: JSON.stringify(jsonData),
        headers: {
            Authorization: jwt
        }
    };
    fetch("position/createMarker", requestOptions)
        .then(response => {
            return response.json()
        })
        .then(data => {
            marker.markerID = data.markerID;
        });
    marker.addListener('drag', function (e) {
        document.getElementById('exampleMarkerLocation').value = e.latLng.lat() + ", " + e.latLng.lng();
        eventHandler(map, geoAT, marker);
    });
    google.maps.event.addListener(geoAT, 'click', function (e) {
        marker.setPosition(e.latLng);
        document.getElementById('exampleMarkerLocation').value = e.latLng.lat() + ", " + e.latLng.lng();
        eventHandler(map, geoAT, marker);
    });
    geoAT.addListener('drag', function () {
        //let distance = bdccGeoDistanceToPolyMtrs(geoAT, marker.getPosition());
        eventHandler(map, geoAT, marker);
    });
    google.maps.event.addListener(map, 'click', function (e) {
        marker.setPosition(e.latLng);
        document.getElementById('exampleMarkerLocation').value = e.latLng.lat() + ", " + e.latLng.lng();
        eventHandler(map, geoAT, marker);
    });

    //  prevMark = marker;
    setTimeout(() => {
        if (google.maps.geometry.poly.containsLocation(locationMarker, geoAT)) {
            document.getElementById('result').innerHTML = "Inside the Polygon!!";
        } else {
            document.getElementById('result').innerHTML = "Outside the Polygon!!";
        }
    }, 1000);
}

function plotOnPointOSM(coordsOSM, autoSearch) {
    let coordsFinal = [];
    for (let i = 0; i < coordsOSM.length; i++) {
        let latLong = coordsOSM[i];
        coordsFinal.push({lat: latLong[1], lng: latLong[0]});
    }
    let mapObjects = {
        zoom: 16,
        center: autoSearch
    };

    let map = new google.maps.Map(document.getElementById('map'), mapObjects);
    let geoAT = new google.maps.Polygon({
        paths: coordsFinal,
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        editable: true,
        draggable: true,
        geodesic: true
    });

    let latLng = new google.maps.LatLng({lat: autoSearch.lat(), lng: autoSearch.lng()});
    if (!(google.maps.geometry.poly.containsLocation(latLng, geoAT))) {
        mapLoc = plotOnPoint(autoSearch);
    } else {
        geoAT.setMap(map);
        mapLoc = {map: map, geoAT: geoAT};
    }

    google.maps.event.addListener(geoAT, 'rightclick', function (mev) {
        if (mev.vertex != null) {
            geoAT.getPath().removeAt(mev.vertex);
        }
    });

    google.maps.event.addListener(geoAT.getPath(), 'set_at', function () {
        console.log(geoAT.getPath().getArray());
    });
    google.maps.event.addListener(geoAT.getPath(), 'insert_at', function () {
        console.log(geoAT.getPath().getArray());
    });

    return mapLoc;
}

function plotOnPoint(center = []) {
    let boundary = [0.001, 0.001];
    let coords = [];
    let coordsFinal = [];

    coords.push([center.lat() + boundary[0], center.lng() - boundary[1]]);
    coords.push([center.lat() + boundary[0], center.lng() + boundary[1]]);
    coords.push([center.lat() - boundary[0], center.lng() + boundary[1]]);
    coords.push([center.lat() - boundary[0], center.lng() - boundary[1]]);

    for (let i = 0; i < coords.length; i++) {
        let latLong = coords[i];
        coordsFinal.push(new google.maps.LatLng({lat: latLong[0], lng: latLong[1]}));

    }
    let mapObjects = {
        zoom: 16,
        center: center,
    };
    let map = new google.maps.Map(document.getElementById('map'), mapObjects);
    let geoAT = new google.maps.Polygon({
        paths: coordsFinal,
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        editable: true,
        draggable: true,
        geodesic: true
    });

    geoAT.setMap(map);
    google.maps.event.addListener(geoAT, 'rightclick', function (mev) {
        if (mev.vertex != null) {
            geoAT.getPath().removeAt(mev.vertex);
        }
    });
    mapLoc = {map: map, geoAT: geoAT};
    return mapLoc;

}

async function checkMaps() {
    let cookieValue = document.cookie;
    jwt = await cookieValue.split(";")[0].split("=")[1].trim();
    let requestOptions = {
        method: 'GET',
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };
    fetch("tenant/getAutocompleteProvider", requestOptions)
        .then(response => {
            return response.text()
        })
        .then(data => {
            if (data == "LocationIQ")
                document.querySelector('#changeAPI').innerHTML = 'Switch To Google';
            else
                document.querySelector('#changeAPI').innerHTML = 'Switch To LocationIQ';
        })

    //Checking if there are any existing polygons in DB or not

    fetch("geofence/checkPoly", requestOptions)
        .then(response => {
            response.text()
                .then(result => {
                    let recordExist = JSON.parse(result);
                    if (!recordExist) {
                        initMaps();
                    } else {
                        let urlParams = new URLSearchParams(window.location.search);
                        let geofenceName = urlParams.get('name');
                        let geofenceId = urlParams.get('id');
                        geofenceDetails = {id: geofenceId, name: geofenceName};
                        if (geofenceId == null) {
                            let center = new google.maps.LatLng({lat: 5.0723, lng: -75.5124});
                            plotOnPoint(center);
                        } else
                            plotGeofenceById();
                    }
                })
        });
}

function initMaps() {
    let location = document.getElementById('exampleInputLocation').value;
    let geoLoc = [];

    if (location !== '') {
        let loc = location.split(',');
        geoLoc = new google.maps.LatLng({lat: parseFloat(loc[0]), lng: parseFloat(loc[1])});
        mapLoc = OSMCallback(geoLoc);
    } else {
        mapLoc = plotLiveLocation();
    }

    document.getElementById("checkButton").addEventListener("click", () => {
        checkMarker();
    });
}

function getInfo() {
    const polyName = document.getElementById('polygonNameInput').value;
    postPolygon(polyName);
    alert("Saved " + polyName + " Geofence");
}

function postPolygon(polyName) {
    let jsonData = {
        "coordinates": mapLoc.geoAT.getPath().getArray(),
        "name": polyName,
        "proximity": proximityThreshold
    };

    let requestOptions = {
        method: 'POST',
        redirect: 'follow',
        body: JSON.stringify(jsonData),
        headers: {
            Authorization: jwt
        }
    };
    fetch("geofence/create", requestOptions)
        .then(response => response.json())
        .then(result => {
            try {
                geofenceDetails.name = result.name;
                geofenceDetails.id = result.id;
            } catch (err) {
                console.log(err);
            }
        })
        .catch(error => console.log('error', error));
}

function plotLiveLocation() {
    document.getElementById("locateButton").disabled = true;
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
            let currentLatitude = position.coords.latitude;
            let currentLongitude = position.coords.longitude;
            let liveLocation = new google.maps.LatLng({lat: currentLatitude, lng: currentLongitude});
            document.getElementById("locateButton").disabled = false;
            document.getElementById("liveCoords").innerHTML = "You are at: " + liveLocation;
            return plotOnPoint(liveLocation);
        });
    }
    return null;
}

function plotGeofenceById() {
    let geofenceId = geofenceDetails.id;
    let geofenceName = geofenceDetails.name;

    if (geofenceId == null || geofenceName == null) {
        alert('Geofence not found');
        return;
    }

    let requestOptions = {
        method: 'GET',
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };

    fetch("geofence/getById?id=" + geofenceId, requestOptions)
        .then(response => response.text())
        .then(result => {
            if (result != null) {
                let geofences = JSON.parse(result);
                plotGeofencePoints(geofences.coordinates);

            } else
                alert("Geofence Not Found");
        })
        .catch(error => console.log('error', error));
}

function plotGeofencePoints(polygonCoordinates) {
    let mapObjects = {
        zoom: 16,
        center: getMeanPoint(polygonCoordinates),
    };
    let map = new google.maps.Map(document.getElementById('map'), mapObjects);
    let geoAT = new google.maps.Polygon({
        paths: polygonCoordinates,
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        editable: true,
        draggable: true,
        geodesic: true
    });

    geoAT.setMap(map);
    let autoLat = geoAT.getPath().getArray()[0].lat();
    let autoLang = geoAT.getPath().getArray()[0].lng();
    document.getElementById('exampleMarkerLocation').value = "" + autoLat + ", " + autoLang;
    document.getElementById('exampleInputLocation').value = "" + autoLat + ", " + autoLang;
    google.maps.event.addListener(geoAT, 'rightclick', function (mev) {
        if (mev.vertex != null) {
            geoAT.getPath().removeAt(mev.vertex);
        }
    });

    mapLoc = {map: map, geoAT: geoAT};
}

function getMeanPoint(polygonCoordinates) {
    let latSum = 0.0;
    let lngSum = 0.0;
    for (let point of polygonCoordinates) {
        latSum += point.lat;
        lngSum += point.lng;
    }
    return {lat: (latSum / polygonCoordinates.length), lng: (lngSum / polygonCoordinates.length)};
}

function goBackToIndex() {
    window.location.href = 'geofenceList.html';
}
