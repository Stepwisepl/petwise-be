package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisionClient {

    public BatchAnnotateImagesResponse detectLabels(List<AnnotateImageRequest> requests) throws Exception {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            return vision.batchAnnotateImages(requests);
        }
    }
}
