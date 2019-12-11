package pl.stepwise.petwise;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.response.VisionClient;
import pl.stepwise.petwise.response.VisionMapper;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;

import java.util.List;

@Service
public class VisionService {

    private final VisionClient visionClient;
    private final VisionMapper visionMapper;

    public VisionService(VisionClient visionClient, VisionMapper visionMapper) {
        this.visionClient = visionClient;
        this.visionMapper = visionMapper;
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
}
