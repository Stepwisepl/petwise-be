package pl.stepwise.petwise.imagecrop.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

@Service
public class Graphics2DCropper {

    public BufferedImage getCroppedImage(BufferedImage bufferedImage, GeneralPath clip, Point2D.Float firstVertex) {
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
