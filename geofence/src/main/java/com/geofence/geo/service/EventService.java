package com.geofence.geo.service;

import com.geofence.geo.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    public static final Logger eventServiceLogger = LoggerFactory.getLogger("Event Service Logger");

    @Autowired
    private Events events;

    public String getAllEvents(String tenantId) {
        eventServiceLogger.info("Inside getAllEvents");
        eventServiceLogger.info(tenantId);
        return events.getEvents();
    }

    public List<String> getEnabledEvents(String tenantId) {
        eventServiceLogger.info("Inside getEnabledEvents");
        eventServiceLogger.info(tenantId);
        List<String> enabledEvents = new ArrayList<>();
        for (Events.Event s : Events.Event.values()) {
            if (s.getState())
                enabledEvents.add(s.name());
        }
        return enabledEvents;
    }

    public void enableEvents(String tenantId, String[] events) {
        eventServiceLogger.info("Inside enableEvents");
        eventServiceLogger.info(tenantId);
        for (String event : events) {
            Events.Event event1 = Events.Event.valueOf(event);
            Events.Event.enableEvents(event1);
        }
    }

    public void disableEvents(String tenantId, String[] events) {
        eventServiceLogger.info("Inside disableEvents");
        eventServiceLogger.info(tenantId);
        for (String event : events) {
            Events.Event event1 = Events.Event.valueOf(event);
            Events.Event.disableEvents(event1);
        }
    }
}
