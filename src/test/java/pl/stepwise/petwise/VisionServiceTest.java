package pl.stepwise.petwise;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.response.VisionClient;
import pl.stepwise.petwise.response.VisionMapper;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.PetwiseLocalizedObject;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisionServiceTest {

    private final String filePath = "test-file";
    private VisionService service;
    @Mock
    private VisionClient clientMock;

    @Mock
    private VisionMapper mapperMock;

    @Mock
    private AnnotateImageResponse responseMock;

    @BeforeEach
    void setUp() {
        service = new VisionService(clientMock, mapperMock);
    }

    @Test
    void shouldReturnLabelList() throws PetwiseImageProcessingException {
        // given
        given(clientMock.detectLabels(filePath))
                .willReturn(responseMock);
        PetwiseLabel expectedLabel = getExpectedLabel();
        given(mapperMock.mapToLabels(responseMock))
                .willReturn(Collections.singletonList(expectedLabel));
        //when
        List<PetwiseLabel> labels = service.detectLabels(filePath);
        //then
        assertAll("labels",
                () -> assertEquals(1, labels.size()),
                () -> assertEquals(expectedLabel, labels.get(0))
        );
    }

    private PetwiseLabel getExpectedLabel() {
        return PetwiseLabel.builder()
                .description("description")
                .mid("mid")
                .score(3.14F)
                .topicality(25.1F)
                .build();
    }

    @Test
    void shouldReturnLocalizedObjects() throws PetwiseImageProcessingException {
        //given
        given(clientMock.localizeObjects(filePath))
                .willReturn(responseMock);
        PetwiseLocalizedObject expectedObject = PetwiseLocalizedObject.builder().objectName("name").build();
        given(mapperMock.mapToLocalizedObjects(responseMock))
                .willReturn(Collections.singletonList(expectedObject));
        //when
        List<PetwiseLocalizedObject> objects = service.localizeObjects(filePath);
        //then
        assertAll("localized objects",
                () -> assertEquals(1, objects.size()),
                () -> assertEquals(expectedObject, objects.get(0))
        );
    }

    @Test
    void shouldReturnCropHints() throws PetwiseImageProcessingException {
        //given
        given(clientMock.getCropHints(filePath))
                .willReturn(responseMock);
        PetwiseCropHint expectedHint = PetwiseCropHint.builder().confidence(22.2F).build();
        given(mapperMock.mapToCropHints(responseMock))
                .willReturn(Collections.singletonList(expectedHint));
        //when
        List<PetwiseCropHint> hints = service.getCropHints(filePath);
        //then
        assertAll("cropping hints",
                () -> assertEquals(1, hints.size()),
                () -> assertEquals(expectedHint, hints.get(0))
        );
    }
}