package pl.stepwise.petwise.imagecrop.service;

import org.springframework.stereotype.Service;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseBoundingPoly;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageCropper {

    private final Graphics2DCropper graphics2DCropper;

    public ImageCropper(Graphics2DCropper graphics2DCropper) {
        this.graphics2DCropper = graphics2DCropper;
    }

    public BufferedImage crop(PetwiseBoundingPoly boundingPoly, BufferedImage bufferedImage) {
        java.util.List<Point.Float> vertices = getVertices(boundingPoly, bufferedImage);
        GeneralPath clip = generatePathFrom(vertices);
        return graphics2DCropper.getCroppedImage(bufferedImage, clip, vertices.get(0));
    }

    private java.util.List<Point.Float> getVertices(PetwiseBoundingPoly boundingPoly, BufferedImage bufferedImage) {
        var imgWidth = bufferedImage.getWidth();
        var imgHeight = bufferedImage.getHeight();
        return boundingPoly.getNormalizedVertices().stream()
                .map(v -> new Point.Float(v.getX() * imgWidth, v.getY() * imgHeight))
                .collect(Collectors.toList());
    }

    private GeneralPath generatePathFrom(List<Point2D.Float> vertices) {
        GeneralPath clip = new GeneralPath();
        var firstVertex = vertices.get(0);
        clip.moveTo(firstVertex.getX(), firstVertex.getY());

        vertices.stream()
                .skip(1)
                .forEach(v -> clip.lineTo(v.getX(), v.getY()));

        clip.closePath();

        clip.transform(AffineTransform.getTranslateInstance(-firstVertex.getX(), -firstVertex.getY()));
        return clip;
    }
}
