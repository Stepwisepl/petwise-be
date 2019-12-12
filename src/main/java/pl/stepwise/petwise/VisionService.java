package pl.stepwise.petwise;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.exception.InvalidLocalizedObjectAmountException;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.image.crop.ImageCropper;
import pl.stepwise.petwise.response.VisionClient;
import pl.stepwise.petwise.response.VisionMapper;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Log4j2
public class VisionService {

    private final VisionClient visionClient;
    private final VisionMapper visionMapper;
    private final ImageCropper imageCropper;
    private final ResourceLoader resourceLoader;

    public VisionService(VisionClient visionClient,
                         VisionMapper visionMapper,
                         ImageCropper imageCropper,
                         ResourceLoader resourceLoader) {
        this.visionClient = visionClient;
        this.visionMapper = visionMapper;
        this.imageCropper = imageCropper;
        this.resourceLoader = resourceLoader;
    }

    public List<PetwiseLabel> detectLabels(String filePath) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.detectLabels(filePath);
        return visionMapper.mapToLabels(response);
    }

    public List<PetwiseLocalizedObject> localizeObjects(String filePath) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.localizeObjects(filePath);
        return visionMapper.mapToLocalizedObjects(response);
    }

    public List<PetwiseCropHint> getCropHints(String filePath) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.getCropHints(filePath);
        return visionMapper.mapToCropHints(response);
    }

    public List<PetwiseLabel> detectLabelsAfterCroppingObject(String filePath) throws PetwiseImageProcessingException, IOException, InvalidLocalizedObjectAmountException {
        long start = System.nanoTime();
        File file = resourceLoader.getResource("classpath:" + filePath).getFile().getAbsoluteFile();
        BufferedImage bufferedImage = ImageIO.read(file);
        PetwiseLocalizedObject eligibleObject = getCategorizedObjects(filePath);
        /**
         * @todo handle multiple eligible objects found, handle no eligible objects found
         */
        if(eligibleObject == null) {
            throw new InvalidLocalizedObjectAmountException("There should be exactly one object eligible for processing.");
        }
        BufferedImage img = imageCropper.crop(eligibleObject.getBoundingPoly(), bufferedImage);

        AnnotateImageResponse responseWithLabels = visionClient.detectLabels(img);
        long end = System.nanoTime();
        long elapsed = end - start;
        log.debug("Elaplsed: " + elapsed/1000000);
        return visionMapper.mapToLabels(responseWithLabels);
    }

    public PetwiseLocalizedObject getCategorizedObjects(String filePath) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.localizeObjects(filePath);
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(response);
        return objects.stream()
                .filter(PetwiseLocalizedObject::hasEligibleCategory)
                .findFirst().orElse(null);
    }
}
