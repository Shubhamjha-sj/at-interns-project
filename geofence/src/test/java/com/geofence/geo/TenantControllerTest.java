package com.geofence.geo;

import com.geofence.geo.service.TenantService;
import com.geofence.geo.controller.TenantController;
import com.geofence.geo.model.Tenant;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TenantController.class)
public class TenantControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TenantService tenantService;
    @MockBean
    RabbitTemplate rabbitTemplate;
    public String tenantId="793c9a87-dda0-4a1a-926c-0925d3ce0d0b";
    @Test
    public void tenatControllerCreateTenantTest() throws Exception {
        Tenant testTenant = new Tenant("Dummy", "password", "temp");
        doNothing().when(tenantService).createTenant(testTenant);
        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/tenant/create/dummyTenant").accept(MediaType.ALL_VALUE).content(MediaType.ALL_VALUE);
        MvcResult result= mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    }
    @Test
    public void tenatControllerGetTenantTest() throws Exception {
        Tenant testTenant = new Tenant("Dummy", "password", "temp");
       when(tenantService.getTenant(tenantId)).thenReturn(testTenant);
        RequestBuilder requestBuilder= MockMvcRequestBuilders.get("/tenant/getTenant/"+tenantId).accept(MediaType.ALL_VALUE);
        MvcResult result= mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    }
    @Test
    public void tenatControllerDeleteByIdTenantTest() throws Exception {

        doNothing().when(tenantService).deleteTenantById(tenantId);
        RequestBuilder requestBuilder= MockMvcRequestBuilders.delete("/tenant/deleteTenant/"+tenantId).accept(MediaType.ALL_VALUE);
        MvcResult result= mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    }
    @Test
    public void tenatControllerDeleteAllTenantTest() throws Exception {

        doNothing().when(tenantService).deleteAllTenant();

        RequestBuilder requestBuilder= MockMvcRequestBuilders.delete("/tenant/deleteAllTenants");
        MvcResult result= mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    }
    @Test
    public void tenatControllerUpdateByIdTenantTest() throws Exception {
       when(tenantService.updateTenant(anyString(),anyString())).thenReturn(new Tenant("Dummy", "password", "temp"));



        RequestBuilder requestBuilder= MockMvcRequestBuilders.put("/tenant/updateTenant/"+tenantId).content("Dummy").contentType(MediaType.ALL_VALUE);
        MvcResult result= mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    }

}
