package com.geofence.geo;


import com.geofence.geo.service.CheckPositionService;
import com.geofence.geo.controller.CheckPositionController;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CheckPositionController.class)
public class CheckPositionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CheckPositionService checkPositionService;
    @MockBean
    RabbitTemplate rabbitTemplate;;
    public String tenantId="/793c9a87-dda0-4a1a-926c-0925d3ce0d0b";
     @Test
    public void geofenceControllerGetPositionTest () throws Exception {
        doNothing().when(rabbitTemplate).convertAndSend(anyString(),anyString(),anyString());

        RequestBuilder request= MockMvcRequestBuilders.post(tenantId+"/markerPosition/savePosition").accept(MediaType.ALL_VALUE).content("{\"polygon\":{\"coordinates\":[{\"lat\":23.0918117,\"lng\":72.5961109},{\"lat\":23.0913804,\"lng\":72.5961856},{\"lat\":23.0909985,\"lng\":72.5964162},{\"lat\":23.0907123,\"lng\":72.5967748},{\"lat\":23.0905561,\"lng\":72.5972183},{\"lat\":23.0905489,\"lng\":72.5976931},{\"lat\":23.0906708,\"lng\":72.5980998},{\"lat\":23.0909024,\"lng\":72.5984456},{\"lat\":23.0912209,\"lng\":72.5986968},{\"lat\":23.0915953,\"lng\":72.5988286},{\"lat\":23.0919888,\"lng\":72.5988282},{\"lat\":23.0923629,\"lng\":72.5986956},{\"lat\":23.092681,\"lng\":72.5984439},{\"lat\":23.092912,\"lng\":72.5980976},{\"lat\":23.0930333,\"lng\":72.5976906},{\"lat\":23.0930329,\"lng\":72.5972628},{\"lat\":23.0929318,\"lng\":72.5969446},{\"lat\":23.0928904,\"lng\":72.596814},{\"lat\":23.0926152,\"lng\":72.5964453},{\"lat\":23.0922406,\"lng\":72.5962011},{\"lat\":23.0918117,\"lng\":72.5961109}],\"marker\":{\"lat\":23.091659861681556,\"lng\":72.59629638385314}},\"event\":{\"currentState\":\"online\",\"timer\":\"17:7:57\",\"marker\":{\"lat\":23.091659861681556,\"lng\":72.59629638385314},\"geofenceDetails\":{\"id\":\"3fa486b9-24c2-48d8-902a-433d9a84d2a3\",\"name\":\"Oracle Tech Park\"}}}")
                .contentType(MediaType.ALL_VALUE) ;
        MvcResult result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Inside",result.getResponse().getContentAsString());
        request=MockMvcRequestBuilders.post(tenantId+"/markerPosition/savePosition").accept(MediaType.ALL_VALUE).content("{\"polygon\":{\"coordinates\":[{\"lat\":23.0918117,\"lng\":72.5961109},{\"lat\":23.0913804,\"lng\":72.5961856},{\"lat\":23.0909985,\"lng\":72.5964162},{\"lat\":23.0907123,\"lng\":72.5967748},{\"lat\":23.0905561,\"lng\":72.5972183},{\"lat\":23.0905489,\"lng\":72.5976931},{\"lat\":23.0906708,\"lng\":72.5980998},{\"lat\":23.0909024,\"lng\":72.5984456},{\"lat\":23.0912209,\"lng\":72.5986968},{\"lat\":23.0915953,\"lng\":72.5988286},{\"lat\":23.0919888,\"lng\":72.5988282},{\"lat\":23.0923629,\"lng\":72.5986956},{\"lat\":23.092681,\"lng\":72.5984439},{\"lat\":23.092912,\"lng\":72.5980976},{\"lat\":23.0930333,\"lng\":72.5976906},{\"lat\":23.0930329,\"lng\":72.5972628},{\"lat\":23.0929318,\"lng\":72.5969446},{\"lat\":23.0928904,\"lng\":72.596814},{\"lat\":23.0926152,\"lng\":72.5964453},{\"lat\":23.0922406,\"lng\":72.5962011},{\"lat\":23.0918117,\"lng\":72.5961109}],\"marker\":{\"lat\":24.091659861681556,\"lng\":73.59629638385314}},\"event\":{\"currentState\":\"online\",\"timer\":\"17:7:57\",\"marker\":{\"lat\":24.091659861681556,\"lng\":73.59629638385314},\"geofenceDetails\":{\"id\":\"3fa486b9-24c2-48d8-902a-433d9a84d2a3\",\"name\":\"Oracle Tech Park\"}}}")
                .contentType(MediaType.ALL_VALUE) ;
        result= mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Outside",result.getResponse().getContentAsString());

    }
}
