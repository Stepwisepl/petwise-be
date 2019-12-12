package pl.stepwise.petwise.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.stepwise.petwise.image.crop.Graphics2DCropper;
import pl.stepwise.petwise.image.crop.ImageCropper;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseBoundingPoly;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseNormalizedVertex;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ImageCropperTest {

    @Mock
    PetwiseBoundingPoly boundingPolyMock;

    @Mock
    BufferedImage bufferedImageMock;

    @Mock
    Graphics2DCropper graphics2DCropperMock;

    private ImageCropper imageCropper;

    @BeforeEach
    void setUp() {
        imageCropper = new ImageCropper(graphics2DCropperMock);
    }

    /**
     * The purpose of this test is to detect changes in logic. It does not test an actual image cropping.
     */
    @Test
    void shouldReturnedTheSameImageAfterMockedCrop() {
        //given
        var imgHeight = 200;
        var imgWidth = 100;
        mockBufferedImage(imgHeight, imgWidth);
        List<PetwiseNormalizedVertex> vertices = getNormalizedVertices();
        given(boundingPolyMock.getNormalizedVertices())
                .willReturn(vertices);
        mockCropping(imgHeight, imgWidth, vertices);
        //when
        BufferedImage cropped = imageCropper.crop(boundingPolyMock, bufferedImageMock);
        //then
        assertAll("mocked crop",
                () -> assertNotNull(cropped),
                () -> assertEquals(imgHeight, cropped.getHeight())
        );
    }

    private void mockBufferedImage(int imgHeight, int imgWidth) {
        given(bufferedImageMock.getHeight())
                .willReturn(imgHeight);
        given(bufferedImageMock.getWidth())
                .willReturn(imgWidth);
    }

    private List<PetwiseNormalizedVertex> getNormalizedVertices() {
        PetwiseNormalizedVertex vertex1 = PetwiseNormalizedVertex.builder().x(0.1F).y(0.10F).build();
        PetwiseNormalizedVertex vertex2 = PetwiseNormalizedVertex.builder().x(0.2F).y(0.1F).build();
        PetwiseNormalizedVertex vertex3 = PetwiseNormalizedVertex.builder().x(0.2F).y(0.2F).build();
        PetwiseNormalizedVertex vertex4 = PetwiseNormalizedVertex.builder().x(0.150F).y(0.2F).build();
        return Arrays.asList(vertex1, vertex2, vertex3, vertex4);
    }

    private void mockCropping(int imgHeight, int imgWidth, List<PetwiseNormalizedVertex> vertices) {
        Point2D.Float firstPoint = new Point.Float(vertices.get(0).getX() * imgWidth, vertices.get(0).getY() * imgHeight);
        given(graphics2DCropperMock.getCroppedImage(eq(bufferedImageMock), any(), eq(firstPoint)))
                .willReturn(bufferedImageMock);
    }
}