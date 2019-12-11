package pl.stepwise.petwise;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.response.ImageCropper;
import pl.stepwise.petwise.response.VisionClient;
import pl.stepwise.petwise.response.VisionMapper;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
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

    public List<PetwiseLabel> detectLabelsForCroppedObject(String filePath) throws PetwiseImageProcessingException, IOException {
        File file = resourceLoader.getResource("classpath:" + filePath).getFile().getAbsoluteFile();
        BufferedImage bufferedImage = ImageIO.read(file);
        BufferedImage img = imageCropper.crop(getEligibleObjects(filePath), bufferedImage);

        AnnotateImageResponse responseWithLabels = visionClient.detectLabels(img);
        return visionMapper.mapToLabels(responseWithLabels);
    }

    private PetwiseLocalizedObject getEligibleObjects(String filePath) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.localizeObjects(filePath);
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(response);
//        List<PetwiseLocalizedObject> eligibleObjects = objects.stream()
        return objects.stream()
                .filter(PetwiseLocalizedObject::hasEligibleCategory)
//                .collect(Collectors.toList());
        .findFirst().orElse(null);
    }
}
