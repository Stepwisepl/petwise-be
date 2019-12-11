package pl.stepwise.petwise.response;

import org.springframework.stereotype.Service;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageCropper {

    public BufferedImage crop(PetwiseLocalizedObject eligibleObject, BufferedImage bufferedImage) {
        java.util.List<Point.Float> vertices = getVertices(eligibleObject, bufferedImage);
        GeneralPath clip = getGeneralPath(vertices);
        return getCroppedImage(bufferedImage, clip, vertices.get(0));
    }

    private java.util.List<Point.Float> getVertices(PetwiseLocalizedObject eligibleObject, BufferedImage bufferedImage) {
        var imgWidth = bufferedImage.getWidth();
        var imgHeight = bufferedImage.getHeight();
        return eligibleObject.getBoundingPoly().getNormalizedVertices().stream()
                .map(v -> new Point.Float(v.getX() * imgWidth, v.getY() * imgHeight))
                .collect(Collectors.toList());
    }

    private GeneralPath getGeneralPath(List<Point2D.Float> vertices) {
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

    private BufferedImage getCroppedImage(BufferedImage bufferedImage, GeneralPath clip, Point2D.Float firstVertex) {
        Rectangle bounds = clip.getBounds();
        BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setClip(clip);
        g2d.translate(-firstVertex.getX(), -firstVertex.getY());
        g2d.drawImage(bufferedImage, 0, 0, null);
        g2d.dispose();
        return img;
    }
}
