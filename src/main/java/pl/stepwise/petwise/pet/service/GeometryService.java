package pl.stepwise.petwise.pet.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.stereotype.Service;

@Service
public class GeometryService {

    private static double DEFAULT_RADIUS = 1000.0;

    public Geometry createCircle(double x, double y, Double radius) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(x, y));
        if(radius == null) {
            radius = DEFAULT_RADIUS;
        }
        shapeFactory.setSize(radius * 2);
        return shapeFactory.createCircle();
    }
}
