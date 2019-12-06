package pl.stepwise.petwise;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.request.RequestFactory;
import pl.stepwise.petwise.response.ResponseProvider;
import pl.stepwise.petwise.response.VisionAnnotation;
import pl.stepwise.petwise.response.VisionAnnotationExtractor;

import java.util.List;

@Service
public class VisionService {

    private final RequestFactory requestFactory;
    private final ResponseProvider responseProvider;
    private final VisionAnnotationExtractor visionAnnotationExtractor;

    public VisionService(RequestFactory requestFactory,
                         ResponseProvider responseProvider,
                         VisionAnnotationExtractor visionAnnotationExtractor) {
        this.requestFactory = requestFactory;
        this.responseProvider = responseProvider;
        this.visionAnnotationExtractor = visionAnnotationExtractor;
    }

    public List<VisionAnnotation> processImage(String filePath) throws Exception {
        List<AnnotateImageRequest> requests = requestFactory.create(filePath);
        List<AnnotateImageResponse> responses = responseProvider.detectLabels(requests);
        return visionAnnotationExtractor.extract(responses);
    }
}
