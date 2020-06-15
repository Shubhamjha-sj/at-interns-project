package com.geofence.geo;



import com.geofence.geo.model.Polygon;
import com.geofence.geo.repository.PolygonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PolygonRepositoryTest {
    @Mock
    PolygonRepository polygonRepository;
    @Test
    public void findAllTest(){
        List<Polygon> polygons=polygonRepository.findAll();
    }
    @Test
    public void findDistinctByIdTest(){
      // Polygon P= polygonRepository.findDistinctById("das23dad");
    }
    @Test
    public void getPolygonByName(){
       // Polygon s= polygonRepository.findDistinctByName("SRM");
    }
    @Test
    public void getAllPolygons(){

        List<Polygon> polygons= polygonRepository.findAll();
    }
    @Test
    public void deletePolygonByIdTest(){
        //Long l=polygonRepository.deletePolygonById("a2n4d3dss");

    }
    @Test
    public void deleteAllPolygonTest(){
      polygonRepository.deleteAll();
    }
    @Test
    public void deletePolygonByNameTest(){
     //   polygonRepository.deletePolygonByName("SRM");

    }


}

