/*
package com.geofence.geo;

import com.geofence.geo.Service.GeofenceService;
import com.geofence.geo.controller.GeofenceController;
import com.geofence.geo.model.Polygon;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GeofenceController.class)
public class GeofenceControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    GeofenceService geofenceService;
    @MockBean
    RabbitTemplate rabbitTemplate;;
    public String tenantId="/793c9a87-dda0-4a1a-926c-0925d3ce0d0b";
    @Test
    public void geofenceControllergetAllNamesTest() throws Exception {
        when(geofenceService.getAllNames()).thenReturn(Arrays.asList(new String[]{"SRM",
                "Cessna",
                "Times Square",
                "Stadium",
                "Oracle Tech Park",
                "Budha Talab",
                "BITS Pilani",
                "BITS Goa"}));
        RequestBuilder request= MockMvcRequestBuilders.get(tenantId+"/geofence/getAllNames").accept(MediaType.ALL_VALUE);
        MvcResult result= mockMvc.perform(request).andExpect(content().json(" [\"SRM\",\"Cessna\",\"Times Square\",\"Stadium\",\"Oracle Tech Park\",\"Budha Talab\",\"BITS Pilani\",\"BITS Goa\"]")).andReturn();
        // System.out.println(result.getResponse().getContentAsString());

    }
    @Test
    public void geofenceControllerCreateGeofenceTest () throws Exception {
        when(geofenceService.create(anyString(),anyString())).thenReturn(new Polygon("5e848dd77f8361668fa4e227","[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]"));
        RequestBuilder request= MockMvcRequestBuilders.post(tenantId+"/geofence/create/").accept(MediaType.ALL_VALUE).content("{\"polygon\":{\"coordinates\":[{\"lat\":23.0918117,\"lng\":72.5961109},{\"lat\":23.0913804,\"lng\":72.5961856},{\"lat\":23.0909985,\"lng\":72.5964162},{\"lat\":23.0907123,\"lng\":72.5967748},{\"lat\":23.0905561,\"lng\":72.5972183},{\"lat\":23.0905489,\"lng\":72.5976931},{\"lat\":23.0906708,\"lng\":72.5980998},{\"lat\":23.0909024,\"lng\":72.5984456},{\"lat\":23.0912209,\"lng\":72.5986968},{\"lat\":23.0915953,\"lng\":72.5988286},{\"lat\":23.0919888,\"lng\":72.5988282},{\"lat\":23.0923629,\"lng\":72.5986956},{\"lat\":23.092681,\"lng\":72.5984439},{\"lat\":23.092912,\"lng\":72.5980976},{\"lat\":23.0930333,\"lng\":72.5976906},{\"lat\":23.0930329,\"lng\":72.5972628},{\"lat\":23.0929318,\"lng\":72.5969446},{\"lat\":23.0928904,\"lng\":72.596814},{\"lat\":23.0926152,\"lng\":72.5964453},{\"lat\":23.0922406,\"lng\":72.5962011},{\"lat\":23.0918117,\"lng\":72.5961109}],\"name\":\"Motera Stadium\"}}")
                .contentType(MediaType.ALL_VALUE) ;
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();

    }
    */
/*@Test
    public void geofenceControllerGetPositionTest () throws Exception {
        doNothing().when(rabbitTemplate).convertAndSend(anyString(),anyString(),anyString());

        RequestBuilder request= MockMvcRequestBuilders.post(tenantId+"/savePosition").accept(MediaType.ALL_VALUE).content("{\"polygon\":{\"coordinates\":[{\"lat\":23.0918117,\"lng\":72.5961109},{\"lat\":23.0913804,\"lng\":72.5961856},{\"lat\":23.0909985,\"lng\":72.5964162},{\"lat\":23.0907123,\"lng\":72.5967748},{\"lat\":23.0905561,\"lng\":72.5972183},{\"lat\":23.0905489,\"lng\":72.5976931},{\"lat\":23.0906708,\"lng\":72.5980998},{\"lat\":23.0909024,\"lng\":72.5984456},{\"lat\":23.0912209,\"lng\":72.5986968},{\"lat\":23.0915953,\"lng\":72.5988286},{\"lat\":23.0919888,\"lng\":72.5988282},{\"lat\":23.0923629,\"lng\":72.5986956},{\"lat\":23.092681,\"lng\":72.5984439},{\"lat\":23.092912,\"lng\":72.5980976},{\"lat\":23.0930333,\"lng\":72.5976906},{\"lat\":23.0930329,\"lng\":72.5972628},{\"lat\":23.0929318,\"lng\":72.5969446},{\"lat\":23.0928904,\"lng\":72.596814},{\"lat\":23.0926152,\"lng\":72.5964453},{\"lat\":23.0922406,\"lng\":72.5962011},{\"lat\":23.0918117,\"lng\":72.5961109}],\"marker\":{\"lat\":23.091659861681556,\"lng\":72.59629638385314}},\"event\":{\"currentState\":\"online\",\"timer\":\"17:7:57\",\"marker\":{\"lat\":23.091659861681556,\"lng\":72.59629638385314},\"geofenceDetails\":{\"id\":\"3fa486b9-24c2-48d8-902a-433d9a84d2a3\",\"name\":\"Oracle Tech Park\"}}}")
                .contentType(MediaType.ALL_VALUE) ;
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Inside",result.getResponse().getContentAsString());
        request=MockMvcRequestBuilders.post(tenantId+"/savePosition").accept(MediaType.ALL_VALUE).content("{\"polygon\":{\"coordinates\":[{\"lat\":23.0918117,\"lng\":72.5961109},{\"lat\":23.0913804,\"lng\":72.5961856},{\"lat\":23.0909985,\"lng\":72.5964162},{\"lat\":23.0907123,\"lng\":72.5967748},{\"lat\":23.0905561,\"lng\":72.5972183},{\"lat\":23.0905489,\"lng\":72.5976931},{\"lat\":23.0906708,\"lng\":72.5980998},{\"lat\":23.0909024,\"lng\":72.5984456},{\"lat\":23.0912209,\"lng\":72.5986968},{\"lat\":23.0915953,\"lng\":72.5988286},{\"lat\":23.0919888,\"lng\":72.5988282},{\"lat\":23.0923629,\"lng\":72.5986956},{\"lat\":23.092681,\"lng\":72.5984439},{\"lat\":23.092912,\"lng\":72.5980976},{\"lat\":23.0930333,\"lng\":72.5976906},{\"lat\":23.0930329,\"lng\":72.5972628},{\"lat\":23.0929318,\"lng\":72.5969446},{\"lat\":23.0928904,\"lng\":72.596814},{\"lat\":23.0926152,\"lng\":72.5964453},{\"lat\":23.0922406,\"lng\":72.5962011},{\"lat\":23.0918117,\"lng\":72.5961109}],\"marker\":{\"lat\":24.091659861681556,\"lng\":73.59629638385314}},\"event\":{\"currentState\":\"online\",\"timer\":\"17:7:57\",\"marker\":{\"lat\":24.091659861681556,\"lng\":73.59629638385314},\"geofenceDetails\":{\"id\":\"3fa486b9-24c2-48d8-902a-433d9a84d2a3\",\"name\":\"Oracle Tech Park\"}}}")
                .contentType(MediaType.ALL_VALUE) ;
        result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Outside",result.getResponse().getContentAsString());

    }*//*

    @Test
    public void geofenceControllerGetByIdTest () throws Exception {
        when(geofenceService.getById(anyString())).thenReturn("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]");
        RequestBuilder request= MockMvcRequestBuilders.get(tenantId+"/geofence/getById").param("id","5e848dd77f8361668fa4e227");
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        //  System.out.println(result.getResponse().getContentAsString());

    }

  */
/*  @Test
    public void geofenceControllerGetPolyByNameTest() throws Exception {
        when(geofenceService.getPolygonByName(anyString())).thenReturn(new Polygon("5e848dd77f8361668fa4e227","[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]"));
        RequestBuilder request= MockMvcRequestBuilders.get(tenantId+"/getByName/{name}","SRM");
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }*//*

    @Test
    public void geofenceControllerDeleteAllPolyTest () throws Exception {
        doNothing().when(geofenceService).deleteAll();
        RequestBuilder request= MockMvcRequestBuilders.delete(tenantId+"/geofence/deleteAll");
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }
    @Test
    public void geofenceControllerDeletePolyByIdTest() throws Exception {
        when(geofenceService.deleteById(anyString())).thenReturn(true);
        RequestBuilder request= MockMvcRequestBuilders.delete(tenantId+"/geofence/delete/{id}","5e848dd77f8361668fa4e227");
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }
   */
/* @Test
    public void geofenceControllerDeletePolyByNameTest() throws Exception {
        when(geofenceService.deletePolygonByName(anyString())).thenReturn("Polygon Deleted Successfully");
        RequestBuilder request= MockMvcRequestBuilders.delete(tenantId+"/deletePolygon/{name}","SRM");
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }*//*

}
*/
