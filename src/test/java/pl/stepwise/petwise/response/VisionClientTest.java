package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gcp.vision.CloudVisionException;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisionClientTest {

    private final String filePath = "test-file";

    private VisionClient visionClient;

    @Mock
    private CloudVisionTemplate cloudVisionTemplateMock;

    @Mock
    private ResourceLoader resourceLoaderMock;

    @Mock
    private Resource resourceMock;

    @BeforeEach
    void setUp() {
        visionClient = new VisionClient(cloudVisionTemplateMock, resourceLoaderMock);
        given(resourceLoaderMock.getResource("classpath:" + filePath))
                .willReturn(resourceMock);
    }

    @Test
    void shouldReturnAnnotateImageResponseWhenAskedForLabels() throws PetwiseImageProcessingException {
        //given
        AnnotateImageResponse expectedResponse = AnnotateImageResponse.newBuilder().build();
        given(cloudVisionTemplateMock.analyzeImage(resourceMock, Feature.Type.LABEL_DETECTION))
                .willReturn(expectedResponse);
        //when
        AnnotateImageResponse response = visionClient.detectLabels(filePath);
        //then
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldWrapVisionExceptionInPetwiseException() {
        //given
        given(cloudVisionTemplateMock.analyzeImage(resourceMock, Feature.Type.LABEL_DETECTION))
                .willThrow(CloudVisionException.class);
        //then
        Assertions.assertThrows(PetwiseImageProcessingException.class, () -> {
            visionClient.detectLabels(filePath);
        });
    }
}