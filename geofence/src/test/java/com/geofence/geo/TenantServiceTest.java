package com.geofence.geo;


import com.geofence.geo.repository.TenantRepository;
import com.geofence.geo.service.TenantService;
import com.geofence.geo.model.Tenant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TenantServiceTest {
    @InjectMocks
    private TenantService tenantService;
    @Mock
  private TenantRepository tenantRepository;
    public String tenantId="793c9a87-dda0-4a1a-926c-0925d3ce0d0b";
    @Test
    public void tenantServiceCreateTenantTest(){
        Tenant testTenant=new Tenant("Dummy", "password", "temp");
        when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
        tenantService.createTenant(testTenant);
    }
    @Test
    public void tenantServiceGetTenantTest(){
        Tenant testTenant=new Tenant("Dummy", "password", "temp");
        when(tenantRepository.getById(anyString())).thenReturn(testTenant);
        tenantService.getTenant(tenantId);
    }
    @Test
    public void tenantServiceDeleteTenantByIdTest(){
        doNothing().when(tenantRepository).deleteTenantById(tenantId);
        tenantService.deleteTenantById(tenantId);
    }
    @Test
    public void tenantServiceDeleteAllTenantsTest(){
        doNothing().when(tenantRepository).deleteAll();
        tenantService.deleteAllTenant();
    }
    @Test
    public void tenantServiceUpdateTenantTest(){
        Tenant testTenant=new Tenant("Dummy", "password", "temp");
        when(tenantRepository.getById(anyString())).thenReturn(testTenant);

        tenantService.updateTenant(tenantId, "Dummy");
    }



}
