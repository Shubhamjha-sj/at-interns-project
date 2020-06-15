package com.geofence.geo.events;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Events {

   public  enum Event{

       @Schema(description = "Enum constants representing events", example = "ENTRY,EXIT,PROXIMITY,DWELL", required = true)
       ENTRY(true), EXIT(true), PROXIMITY(true),DWELL(true);
       @Schema(description = "Represents whether a state is enabled or not", example = "true,false", required = true)
       private boolean state;
       Event (boolean b){
           state=b;
       }
       public boolean getState(){
           return state;
       }
       public static void enableEvents(Event event){
           event.state=true;
       }
       public static void disableEvents(Event event){
           event.state=false;
       }


   }
   public String getEvents(){
       return Arrays.toString(Event.values());
   }
}
