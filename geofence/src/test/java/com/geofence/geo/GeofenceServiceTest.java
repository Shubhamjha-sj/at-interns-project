/*
package com.geofence.geo;


import com.geofence.geo.Repository.PolygonRepository;
import com.geofence.geo.Service.GeofenceService;
import com.geofence.geo.model.Polygon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
public class GeofenceServiceTest {
    @InjectMocks
    private GeofenceService geofenceService;
    @Mock
    private PolygonRepository polygonRepository;
   */
/* @Test
    public void geofenceServiceGetAllTest(){
        when(polygonRepository.findAll()).thenReturn(Arrays.asList(new Polygon("5e848dd77f8361668fa4e227","[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM"),
                new Polygon("3fa486b9-24c2-48d8-902a-433d9a84d2a3","[{\"lat\":12.9343171,\"lng\":77.6015345},{\"lat\":12.9341273,\"lng\":77.6027107},{\"lat\":12.9340725,\"lng\":77.6027014},{\"lat\":12.9340211,\"lng\":77.6030195},{\"lat\":12.9343006,\"lng\":77.603067},{\"lat\":12.9343492,\"lng\":77.6027667},{\"lat\":12.9343161,\"lng\":77.6027001},{\"lat\":12.9344992,\"lng\":77.6015654},{\"lat\":12.9343171,\"lng\":77.6015345}]","Oracle Tech Park")
        ));
        List<Polygon>polygons=geofenceService.getAllPolygon();
        assertEquals("SRM",polygons.get(0).getName());
        assertEquals("Oracle Tech Park",polygons.get(1).getName());
        //System.out.println(polygons);
    }*//*

   @Test
   public void geofenceServiceCreatePolygonTest(){
       Polygon p=new Polygon("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM");
        when(polygonRepository.save(any(Polygon.class))).thenReturn(p);


        Polygon poly=geofenceService.create("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM");
       System.out.println(poly);
   }

    @Test
    public void geofenceServiceGetPolygonByIdTest(){
        when(polygonRepository.findDistinctById(anyString())).thenReturn( new Polygon("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM"));
        String s=geofenceService.getById("5e848dd77f8361668fa4e227");
        assertNotNull(s);

    }
    @Test
    public void geofenceServiceGetPolygonByNameTest(){
        when(polygonRepository.findDistinctByName(anyString())).thenReturn( new Polygon("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM"));
        Polygon p=geofenceService.getByName("SRM");
        assertNotNull(p);

    }


    @Test
    public void  geofenceServiceDeletePolygonByIdTest(){
        when(polygonRepository.deletePolygonById(anyString())).thenReturn(1L);
        geofenceService.deleteById("5e848dd77f8361668fa4e227");
    }
    @Test
    public void geofenceServiceDeleteAllPolygonTest(){
        doNothing().when(polygonRepository).deleteAll();
        geofenceService.deleteAll();
    }
   */
/* @Test
    public void geofenceServiceGetPaginatedTest(){
       when(polygonRepository.findAll(any(Pageable.class))).thenReturn(Arrays.asList(new Polygon("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM")));
        Page<Polygon> pagedResult = (Page<Polygon>) geofenceService.getPaginated(0,10,"name");
        assertNotNull(pagedResult);
    }*//*

    @Test
    public void geofenceServiceGetAllNames(){
        when(polygonRepository.findAll()).thenReturn(Arrays.asList(new Polygon("[{\"lat\":12.8229032,\"lng\":80.0409581},{\"lat\":12.8225325,\"lng\":80.0410526},{\"lat\":12.8217068,\"lng\":80.0411811},{\"lat\":12.8216896,\"lng\":80.0432154},{\"lat\":12.8198903,\"lng\":80.0432154},{\"lat\":12.8198066,\"lng\":80.0499789},{\"lat\":12.8217733,\"lng\":80.050039},{\"lat\":12.8239493,\"lng\":80.0507857},{\"lat\":12.8251879,\"lng\":80.0516697},{\"lat\":12.8253302,\"lng\":80.0508973},{\"lat\":12.8263344,\"lng\":80.0509402},{\"lat\":12.8267287,\"lng\":80.049119},{\"lat\":12.8263305,\"lng\":80.0490991},{\"lat\":12.8264337,\"lng\":80.0481654},{\"lat\":12.8264134,\"lng\":80.0478812},{\"lat\":12.8261228,\"lng\":80.0478604},{\"lat\":12.8261252,\"lng\":80.0473353},{\"lat\":12.8252214,\"lng\":80.0472924},{\"lat\":12.8252214,\"lng\":80.0457045},{\"lat\":12.8257068,\"lng\":80.0456616},{\"lat\":12.8257235,\"lng\":80.044829},{\"lat\":12.8273638,\"lng\":80.0447775},{\"lat\":12.8273805,\"lng\":80.0427004},{\"lat\":12.8257904,\"lng\":80.0411555},{\"lat\":12.8229032,\"lng\":80.0409581}]","SRM")));
        List<Polygon> polygonListTest=polygonRepository.findAll();
        List<String> polyNameListTest= new ArrayList<>();
        for (Polygon polygon :polygonListTest ) {
            polyNameListTest.add(polygon.getName());
        }
        assertNotNull(polyNameListTest);
    }
   */
/* @Test
        when(polygonRepository.deletePolygonByName(anyString())).thenReturn(1L);

       geofenceService.deleteByName("SRM");
    }*//*




}

*/
