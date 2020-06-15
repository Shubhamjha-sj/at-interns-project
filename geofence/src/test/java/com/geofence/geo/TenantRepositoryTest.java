package com.geofence.geo;


import com.geofence.geo.model.Tenant;
import com.geofence.geo.repository.TenantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TenantRepositoryTest {
    @Mock
    TenantRepository tenantRepository;
    public String tenantId="793c9a87-dda0-4a1a-926c-0925d3ce0d0b";
    @Test
    public void tenantRepositoryCreateTenantTest(){
        Tenant tenant=new Tenant("Dummy", "password", "temp");
        tenantRepository.save(tenant);
    }
    @Test
    public void tenantRepositoryGetTenantById(){
        Tenant tenant= tenantRepository.getById(tenantId);
    }
    @Test
    public void tenantRepositoryDeleteTenantById(){
        tenantRepository.deleteTenantById(tenantId);
    }
    @Test
    public void tenantRepositoryDeleteAllTenants(){
        tenantRepository.deleteAll();
    }
}
