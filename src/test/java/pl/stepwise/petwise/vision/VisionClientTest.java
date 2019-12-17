package pl.stepwise.petwise.vision;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gcp.vision.CloudVisionException;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ByteArrayResource;
import pl.stepwise.petwise.vision.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.vision.service.VisionClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisionClientTest {

    private VisionClient visionClient;

    private ByteArrayResource byteArrayResource;

    @Mock
    private CloudVisionTemplate cloudVisionTemplateMock;

    @BeforeEach
    void setUp() {
        visionClient = new VisionClient(cloudVisionTemplateMock);
         byteArrayResource = new ByteArrayResource(new byte[0]);
    }

    @Test
    void shouldReturnAnnotateImageResponseWhenAskedForLabels() throws PetwiseImageProcessingException {
        //given
        AnnotateImageResponse expectedResponse = AnnotateImageResponse.newBuilder().build();
        given(cloudVisionTemplateMock.analyzeImage(byteArrayResource, Feature.Type.LABEL_DETECTION))
                .willReturn(expectedResponse);
        //when
        AnnotateImageResponse response = visionClient.detectLabels(byteArrayResource);
        //then
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldWrapVisionExceptionInPetwiseException() {
        //given
        given(cloudVisionTemplateMock.analyzeImage(byteArrayResource, Feature.Type.LABEL_DETECTION))
                .willThrow(CloudVisionException.class);
        //then
        Assertions.assertThrows(PetwiseImageProcessingException.class, () -> {
            visionClient.detectLabels(byteArrayResource);
        });
    }
}