package com.geofence.geo;

import com.geofence.geo.controller.CheckPositionController;
import com.geofence.geo.events.Coordinates;
import com.geofence.geo.events.GeoUtil;
import com.geofence.geo.model.Marker;
import com.geofence.geo.model.Polygon;
import com.geofence.geo.service.CheckPositionService;
import com.google.gson.*;
import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.geometrylib.PolyUtils;
import com.google.gwt.maps.client.overlays.PolygonOptions;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)

public class PerformanceTest {
    @Autowired
    private CheckPositionService checkPositionService;
    @Test
    public void runBenchmarks() throws RunnerException {
        Options opts = new OptionsBuilder()
                // set the class name regex for benchmarks to search for to the current class
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(3)
                .measurementIterations(3)
                // do not use forking or the benchmark methods will not see references stored within its class
                .forks(0)
                // do not use multiple threads
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();

        new Runner(opts).run();

    }
  /*  @Benchmark
    public String getPosition() {
        String polyInfo="{\"polygon\":{\"coordinates\":[{\"lat\":12.9330133,\"lng\":77.6913295},{\"lat\":12.9322979,\"lng\":77.6916524},{\"lat\":12.9327066,\"lng\":77.6932724},{\"lat\":12.9325733,\"lng\":77.6933099},{\"lat\":12.9313527,\"lng\":77.6933592},{\"lat\":12.9313439,\"lng\":77.6934175},{\"lat\":12.931332,\"lng\":77.6934898},{\"lat\":12.9313028,\"lng\":77.6944407},{\"lat\":12.9313125,\"lng\":77.6945347},{\"lat\":12.9313033,\"lng\":77.6950278},{\"lat\":12.9315582,\"lng\":77.6952158},{\"lat\":12.9317168,\"lng\":77.6954066},{\"lat\":12.9318559,\"lng\":77.6957104},{\"lat\":12.9320107,\"lng\":77.6962523},{\"lat\":12.932106,\"lng\":77.6963606},{\"lat\":12.9321534,\"lng\":77.6963739},{\"lat\":12.933489,\"lng\":77.6966936},{\"lat\":12.9336966,\"lng\":77.6967433},{\"lat\":12.9350973,\"lng\":77.6970785},{\"lat\":12.9355303,\"lng\":77.697143},{\"lat\":12.935911,\"lng\":77.6971997},{\"lat\":12.9364843,\"lng\":77.6973799},{\"lat\":12.9366113,\"lng\":77.6974175},{\"lat\":12.9369131,\"lng\":77.6975068},{\"lat\":12.9373535,\"lng\":77.6976372},{\"lat\":12.9375322,\"lng\":77.6976693},{\"lat\":12.9376238,\"lng\":77.6973708},{\"lat\":12.9374204,\"lng\":77.6973134},{\"lat\":12.9374256,\"lng\":77.6972866},{\"lat\":12.9372081,\"lng\":77.6972742},{\"lat\":12.9368897,\"lng\":77.6972946},{\"lat\":12.936999,\"lng\":77.6963972},{\"lat\":12.9366754,\"lng\":77.6963398},{\"lat\":12.9367666,\"lng\":77.6957447},{\"lat\":12.9368218,\"lng\":77.6953849},{\"lat\":12.9378439,\"lng\":77.6955995},{\"lat\":12.9380521,\"lng\":77.6945906},{\"lat\":12.9382139,\"lng\":77.6943894},{\"lat\":12.9378264,\"lng\":77.6939706},{\"lat\":12.9374871,\"lng\":77.6936053},{\"lat\":12.9365985,\"lng\":77.6926245},{\"lat\":12.935768,\"lng\":77.6917214},{\"lat\":12.9356518,\"lng\":77.6917664},{\"lat\":12.9353573,\"lng\":77.6918803},{\"lat\":12.9354514,\"lng\":77.6931812},{\"lat\":12.9346443,\"lng\":77.6933035},{\"lat\":12.9346073,\"lng\":77.6932906},{\"lat\":12.9340681,\"lng\":77.6933298},{\"lat\":12.9335857,\"lng\":77.6933309},{\"lat\":12.9334912,\"lng\":77.6933298},{\"lat\":12.933468,\"lng\":77.6932633},{\"lat\":12.9330133,\"lng\":77.6913295}],\"marker\":{\"lat\":12.932896687300232,\"lng\":77.69148357181405}},\"event\":{\"currentState\":\"online\",\"timer\":\"10:38:9\",\"marker\":{\"lat\":12.932896687300232,\"lng\":77.69148357181405},\"geofenceDetails\":{\"id\":\"326dafa1-c1b4-487a-9b1f-cfdeca9ec86f\",\"name\":\"Cessna\"}}}";

        String currPosition;
        Gson gson=new Gson();
        Marker marker= gson.fromJson(polyInfo,Marker.class);
        String geofenceID=marker.getGeofenceID();
        String prevPosition=marker.getCurrentState();
        Polygon polygon=checkPositionService.getById(geofenceID);

        if (GeoUtil.contains(polygon.getCoordinates(), marker.getMarkerPosition())) {
            currPosition = "Inside";
        } else {
            currPosition = "Outside";
        }

        return currPosition;
    }*/
/*    @Benchmark
    public String getPositionFromGoogleAPI() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.eval("var x = 10;");


    }*/
    /*@State(value = Scope.Benchmark)
    public static class Parameters {

        @Param({"1", "1000"})
        String batchSize;
    }*/

}
