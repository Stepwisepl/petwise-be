package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.PetwiseLocalizedObject;
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
    CropHintsAnnotation cropHintsAnnotationMock;

    @Mock
    CropHint cropHintMock;

    private VisionMapper visionMapper;

    @BeforeEach
    void setUp() {
        visionMapper = new VisionMapper();
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
        given(localizedObjectAnnotationMock.getName())
                .willReturn("name");
        //when
        List<PetwiseLocalizedObject> objects = visionMapper.mapToLocalizedObjects(annotateImageResponseMock);
        //then
        assertAll("localized objects",
                () -> assertEquals(1, objects.size()),
                () -> assertEquals("name", objects.get(0).getObjectName())
        );
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