package pl.stepwise.petwise;

import com.google.cloud.storage.Blob;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;
import pl.stepwise.petwise.vision.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.imagecrop.service.ImageCropper;
import pl.stepwise.petwise.vision.service.VisionClient;
import pl.stepwise.petwise.vision.service.VisionService;
import pl.stepwise.petwise.vision.service.VisionToPetwiseMapper;
import pl.stepwise.petwise.vision.model.PetwiseCropHint;
import pl.stepwise.petwise.vision.model.PetwiseLabel;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseLocalizedObject;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisionServiceTest {

    private final String fileId = "1234";
    private VisionService service;
    @Mock
    private VisionClient clientMock;

    @Mock
    private VisionToPetwiseMapper mapperMock;

    @Mock
    ImageCropper imageCropperMock;

    @Mock
    private AnnotateImageResponse responseMock;

    @Mock
    private GoogleStorageService storageService;

    @Mock
    private Blob blobMock;

    @BeforeEach
    void setUp() {
        service = new VisionService(clientMock, mapperMock, imageCropperMock, storageService);
        given(storageService.getFile(fileId))
                .willReturn(blobMock);
        var imageBytes = new byte[1];
        given(blobMock.getContent())
                .willReturn(imageBytes);
    }

    @Test
    void shouldReturnLabelList() throws PetwiseImageProcessingException {
        // given
        given(clientMock.detectLabels(any(ByteArrayResource.class)))
                .willReturn(responseMock);
        PetwiseLabel expectedLabel = getExpectedLabel();
        given(mapperMock.mapToLabels(responseMock))
                .willReturn(Collections.singletonList(expectedLabel));
        //when
        List<PetwiseLabel> labels = service.detectLabels(fileId);
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
        given(clientMock.localizeObjects(any(ByteArrayResource.class)))
                .willReturn(responseMock);
        PetwiseLocalizedObject expectedObject = PetwiseLocalizedObject.builder().objectName("name").build();
        given(mapperMock.mapToLocalizedObjects(responseMock))
                .willReturn(Collections.singletonList(expectedObject));
        //when
        List<PetwiseLocalizedObject> objects = service.localizeObjects(fileId);
        //then
        assertAll("localized objects",
                () -> assertEquals(1, objects.size()),
                () -> assertEquals(expectedObject, objects.get(0))
        );
    }

    @Test
    void shouldReturnCropHints() throws PetwiseImageProcessingException {
        //given
        given(clientMock.getCropHints(any(ByteArrayResource.class)))
                .willReturn(responseMock);
        PetwiseCropHint expectedHint = PetwiseCropHint.builder().confidence(22.2F).build();
        given(mapperMock.mapToCropHints(responseMock))
                .willReturn(Collections.singletonList(expectedHint));
        //when
        List<PetwiseCropHint> hints = service.getCropHints(fileId);
        //then
        assertAll("cropping hints",
                () -> assertEquals(1, hints.size()),
                () -> assertEquals(expectedHint, hints.get(0))
        );
    }
}