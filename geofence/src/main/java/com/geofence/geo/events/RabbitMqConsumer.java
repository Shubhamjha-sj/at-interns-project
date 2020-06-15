package com.geofence.geo.events;

import com.geofence.geo.model.Marker;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.geofence.geo.constants.StringConstants.*;

@Service
public class RabbitMqConsumer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Logger in RabbitMQConsumer");
    @RabbitListener(queues = {EXIT_QUEUE})
    public void getMessageForExit(String eventInfo) {
          Gson gson=new Gson();
          Marker marker= gson.fromJson(eventInfo,Marker.class);

        String geofenceID=marker.getGeofenceID();
        String geofenceName=marker.getGeofenceName();
        Double distance=marker.getDistance();

        LOGGER.info("Exit Event\nState Changed:\nMarker ID:{}\nCurrent State:{}\nTime:{}\nAt Location:({},{})\nGeofenceId:{}\nGeofence Name:{}\nDistance from Geofence:{}", marker.getMarkerID(),marker.getCurrentState(), marker.getTimeStamp(), marker.getMarkerPosition().getLat(), marker.getMarkerPosition().getLng(), geofenceID, geofenceName,String.format("%.2f",distance)+"m");
    }

    @RabbitListener(queues = ENTER_QUEUE)
    public void getMessageForEnter(String eventInfo) {

        Gson gson=new Gson();
        Marker marker= gson.fromJson(eventInfo,Marker.class);
        String state = marker.getCurrentState();
        LOGGER.info(state);
        String geofenceID=marker.getGeofenceID();
        String geofenceName=marker.getGeofenceName();
        Double distance=marker.getDistance();
        LOGGER.info("Enter Event\nState Changed:\nMarker ID:{}\nCurrent State:{}\nTime:{}\nAt Location:({},{})\nGeofenceId:{}\nGeofence Name:{}\nDistance from Geofence:{}", marker.getMarkerID(),marker.getCurrentState(), marker.getTimeStamp(), marker.getMarkerPosition().getLat(), marker.getMarkerPosition().getLng(), geofenceID, geofenceName,String.format("%.2f",distance)+"m");

    }
    @RabbitListener(queues = DWELL_QUEUE)
    public void getMessageForDwell(String eventInfo) {

        Gson gson=new Gson();
        Marker marker= gson.fromJson(eventInfo,Marker.class);
        String state = marker.getCurrentState();
        LOGGER.info(state);
        String geofenceID=marker.getGeofenceID();
        String geofenceName=marker.getGeofenceName();
        Double distance=marker.getDistance();
        LOGGER.info("Dwell Event\nState Changed:\nMarker ID:{}\nCurrent State:{}\nTime:{}\nAt Location:({},{})\nGeofenceId:{}\nGeofence Name:{}\nDistance from Geofence:{}", marker.getMarkerID(),marker.getCurrentState(), marker.getTimeStamp(), marker.getMarkerPosition().getLat(), marker.getMarkerPosition().getLng(), geofenceID, geofenceName,String.format("%.2f",distance)+"m");

    }

}

