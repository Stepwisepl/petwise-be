package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseProvider {

    private final VisionClient visionClient;

    public ResponseProvider(VisionClient visionClient) {
        this.visionClient = visionClient;
    }

    public List<AnnotateImageResponse> detectLabels(List<AnnotateImageRequest> requests)
            throws Exception {
        return visionClient.detectLabels(requests).getResponsesList();
    }
}
