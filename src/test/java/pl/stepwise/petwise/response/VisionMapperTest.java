package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.stepwise.petwise.response.domain.Category;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisionMapperTest {

    @Mock
    AnnotateImageResponse annotateImageResponseMock;

    @Mock
    EntityAnnotation entityAnnotationMock;

    @Mock
    LocalizedObjectAnnotation localizedObjectAnnotationMock;

    @Mock
    BoundingPoly boundingPolyMock;

    @Mock
    NormalizedVertex normalizedVertexMock;

    @Mock
    CropHintsAnnotation cropHintsAnnotationMock;

    @Mock
    CropHint cropHintMock;

    @Mock
    private LocalizedObjectClassifier classifier;

    private VisionMapper visionMapper;

    @BeforeEach
    void setUp() {
        visionMapper = new VisionMapper(classifier);
    }

    @Test
    void shouldMapVisionResponseToLabelList() {
        //given
        given(annotateImageResponseMock.getLabelAnnotationsList())
                .willReturn(Collections.singletonList(entityAnnotationMock));
        mockLabels();
        //when
        List<PetwiseLabel> labels = visionMapper.mapToLabels(annotateImageResponseMock);
        //then
        assertAll("labels",
                () -> assertEquals(1, labels.size()),
                () -> assertEquals("description", labels.get(0).getDescription()),
                () -> assertEquals("mid", labels.get(0).getMid()),
                () -> assertEquals(3.14F, labels.get(0).getTopicality()),
                () -> assertEquals(25.1F, labels.get(0).getScore())
        );
    }

    private void mockLabels() {
        given(entityAnnotationMock.getDescription())
                .willReturn("description");
        given(entityAnnotationMock.getMid())
                .willReturn("mid");
        given(entityAnnotationMock.getTopicality())
                .willReturn(3.14F);
        given(entityAnnotationMock.getScore())
                .willReturn(25.1F);
    }

    @Test
    void shouldMapVisionResponseToObjectList() {
        //given
        given(annotateImageResponseMock.getLocalizedObjectAnnotationsList())
                .willReturn(Collections.singletonList(localizedObjectAnnotationMock));
        mockLocalizedObject();
        given(classifier.assignCategory("Dog"))
                .willReturn(Category.ANIMALS);
        //when
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(annotateImageResponseMock);
        //then
        assertAll("localized objects",
                () -> assertEquals(1, objects.size()),
                () -> assertEquals("Dog", objects.get(0).getObjectName()),
                () -> assertEquals(Category.ANIMALS, objects.get(0).getCategory()),
                () -> assertEquals(1.1F, objects.get(0).getBoundingPoly().getNormalizedVertices().get(0).getX()),
                () -> assertEquals(2.2F, objects.get(0).getBoundingPoly().getNormalizedVertices().get(0).getY()),
                () -> assertEquals(1, objects.get(0).getBoundingPoly().getNormalizedVertices().size())
        );
    }

    private void mockLocalizedObject() {
        given(localizedObjectAnnotationMock.getName())
                .willReturn("Dog");
        given(localizedObjectAnnotationMock.getBoundingPoly())
                .willReturn(boundingPolyMock);
        given(boundingPolyMock.getNormalizedVerticesList())
                .willReturn(Collections.singletonList(normalizedVertexMock));
        given(normalizedVertexMock.getX())
                .willReturn(1.1F);
        given(normalizedVertexMock.getY())
                .willReturn(2.2F);
    }

    @Test
    void shouldMapVisionResponseToCropHintList() {
        //given
        given(annotateImageResponseMock.getCropHintsAnnotation())
                .willReturn(cropHintsAnnotationMock);
        given(cropHintsAnnotationMock.getCropHintsList())
                .willReturn(Collections.singletonList(cropHintMock));
        given(cropHintMock.getConfidence())
                .willReturn(22.2F);
        //when
        List<PetwiseCropHint> hints = visionMapper.mapToCropHints(annotateImageResponseMock);
        //then
        assertAll("crop hints",
                () -> assertEquals(1, hints.size()),
                () -> assertEquals(22.2F, hints.get(0).getConfidence())
        );
    }
}