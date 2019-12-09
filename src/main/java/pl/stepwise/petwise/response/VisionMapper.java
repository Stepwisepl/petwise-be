package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.PetwiseLocalizedObject;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisionMapper {

    public List<PetwiseLabel> mapToLabels(AnnotateImageResponse response) {
        return response.getLabelAnnotationsList().stream()
                .map(a -> PetwiseLabel.builder()
                        .description(a.getDescription())
                        .mid(a.getMid())
                        .topicality(a.getTopicality())
                        .score(a.getScore())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PetwiseLocalizedObject> mapToLocalizedObjects(AnnotateImageResponse response) {
        return response.getLocalizedObjectAnnotationsList().stream()
                .map(a -> PetwiseLocalizedObject.builder()
                        .objectName(a.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PetwiseCropHint> mapToCropHints(AnnotateImageResponse response) {
        return response.getCropHintsAnnotation().getCropHintsList().stream()
                .map(c -> PetwiseCropHint.builder()
                .confidence(c.getConfidence())
                .build())
                .collect(Collectors.toList());
    }
}
