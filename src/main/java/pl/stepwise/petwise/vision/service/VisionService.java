package pl.stepwise.petwise.vision.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;
import pl.stepwise.petwise.imagecrop.service.ImageCropper;
import pl.stepwise.petwise.vision.exception.InvalidLocalizedObjectAmountException;
import pl.stepwise.petwise.vision.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.vision.model.PetwiseCropHint;
import pl.stepwise.petwise.vision.model.PetwiseLabel;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseLocalizedObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
@Log4j2
public class VisionService {

    private final VisionClient visionClient;
    private final VisionToPetwiseMapper visionMapper;
    private final ImageCropper imageCropper;
    private final GoogleStorageService storageService;

    public VisionService(VisionClient visionClient,
                         VisionToPetwiseMapper visionMapper,
                         ImageCropper imageCropper,
                         GoogleStorageService storageService) {
        this.visionClient = visionClient;
        this.visionMapper = visionMapper;
        this.imageCropper = imageCropper;
        this.storageService = storageService;
    }

    public List<PetwiseLabel> detectLabels(String fileId) throws PetwiseImageProcessingException {
        var resource = new ByteArrayResource(storageService.getFile(fileId).getContent());
        AnnotateImageResponse response = visionClient.detectLabels(resource);
        return visionMapper.mapToLabels(response);
    }

    public List<PetwiseLocalizedObject> localizeObjects(String fileId) throws PetwiseImageProcessingException {
        var resource = new ByteArrayResource(storageService.getFile(fileId).getContent());
        AnnotateImageResponse response = visionClient.localizeObjects(resource);
        return visionMapper.mapToLocalizedObjects(response);
    }

    public List<PetwiseCropHint> getCropHints(String fileId) throws PetwiseImageProcessingException {
        var resource = new ByteArrayResource(storageService.getFile(fileId).getContent());
        AnnotateImageResponse response = visionClient.getCropHints(resource);
        return visionMapper.mapToCropHints(response);
    }

    public List<PetwiseLabel> detectLabelsAfterCroppingObject(String fileId) throws PetwiseImageProcessingException, IOException, InvalidLocalizedObjectAmountException {
        long start = System.nanoTime();
        var resource = new ByteArrayResource(storageService.getFile(fileId).getContent());
        BufferedImage bufferedImage = ImageIO.read(resource.getInputStream());
        PetwiseLocalizedObject eligibleObject = getEligibleObject(resource);
        /**
         * @todo handle multiple eligible objects found, handle no eligible objects found
         */
        if (eligibleObject == null) {
            throw new InvalidLocalizedObjectAmountException("There should be exactly one object eligible for processing.");
        }
        BufferedImage img = imageCropper.crop(eligibleObject.getBoundingPoly(), bufferedImage);

        AnnotateImageResponse responseWithLabels = visionClient.detectLabels(img);
        long end = System.nanoTime();
        long elapsed = end - start;
        log.debug("Elapsed: " + elapsed / 1000000);
        return visionMapper.mapToLabels(responseWithLabels);
    }

    public PetwiseLocalizedObject getEligibleObject(ByteArrayResource resource) throws PetwiseImageProcessingException {
        AnnotateImageResponse response = visionClient.localizeObjects(resource);
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(response);
        return objects.stream()
                .filter(PetwiseLocalizedObject::hasEligibleCategory)
                .findFirst().orElse(null);
    }

    public PetwiseLocalizedObject getEligibleObject(String fileId) throws PetwiseImageProcessingException {
        var resource = new ByteArrayResource(storageService.getFile(fileId).getContent());
        AnnotateImageResponse response = visionClient.localizeObjects(resource);
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(response);
        return objects.stream()
                .filter(PetwiseLocalizedObject::hasEligibleCategory)
                .findFirst().orElse(null);
    }
}
