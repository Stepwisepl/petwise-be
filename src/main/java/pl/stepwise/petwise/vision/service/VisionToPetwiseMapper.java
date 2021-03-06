package pl.stepwise.petwise.vision.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.NormalizedVertex;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.vision.model.PetwiseCropHint;
import pl.stepwise.petwise.vision.model.PetwiseLabel;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseBoundingPoly;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseLocalizedObject;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseNormalizedVertex;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisionToPetwiseMapper {

    private final LocalizedObjectClassifier classifier;

    public VisionToPetwiseMapper(LocalizedObjectClassifier classifier) {
        this.classifier = classifier;
    }

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
                        .boundingPoly(mapToBoundingPoly(a.getBoundingPoly()))
                        .category(classifier.assignCategory(a.getName()))
                        .build())
                .collect(Collectors.toList());
    }

    private PetwiseBoundingPoly mapToBoundingPoly(BoundingPoly visionBoundingPoly) {
        return PetwiseBoundingPoly.builder()
                .normalizedVertices(mapToNormalizedVertices(visionBoundingPoly.getNormalizedVerticesList()))
                .build();
    }

    private List<PetwiseNormalizedVertex> mapToNormalizedVertices(List<NormalizedVertex> visionVertices) {
        return visionVertices.stream()
                .map(v -> PetwiseNormalizedVertex.builder()
                        .x(v.getX())
                        .y(v.getY())
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
