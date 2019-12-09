package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gcp.vision.CloudVisionException;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;

@Service
@Log4j2
public class VisionClient {
    private final CloudVisionTemplate cloudVisionTemplate;
    private final ResourceLoader resourceLoader;

    public VisionClient(CloudVisionTemplate cloudVisionTemplate, ResourceLoader resourceLoader) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.resourceLoader = resourceLoader;
    }

    public AnnotateImageResponse detectLabels(String filePath) throws PetwiseImageProcessingException {
        try {
            return this.cloudVisionTemplate.analyzeImage(
                    this.resourceLoader.getResource("classpath:" + filePath), Feature.Type.LABEL_DETECTION);
        } catch (CloudVisionException e) {
            throw new PetwiseImageProcessingException("An error occurred while processing image labels.", e);
        }
    }

    public AnnotateImageResponse localizeObjects(String filePath) throws PetwiseImageProcessingException {
        try {
            return this.cloudVisionTemplate.analyzeImage(
                    this.resourceLoader.getResource("classpath:" + filePath), Feature.Type.OBJECT_LOCALIZATION);
        } catch (CloudVisionException e) {
            log.error(e);
            throw new PetwiseImageProcessingException("An error occurred while processing objects localization.", e);
        }
    }

    public AnnotateImageResponse getCropHints(String filePath) throws PetwiseImageProcessingException {
        try {
            return this.cloudVisionTemplate.analyzeImage(
                    this.resourceLoader.getResource("classpath:" + filePath), Feature.Type.CROP_HINTS);
        } catch (CloudVisionException e) {
            log.error(e);
            throw new PetwiseImageProcessingException("An error occurred while getting cropping hints.", e);
        }
    }
}
