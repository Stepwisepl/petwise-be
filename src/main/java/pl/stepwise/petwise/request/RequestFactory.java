package pl.stepwise.petwise.request;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestFactory {

    private final FileReader fileReader;

    public RequestFactory(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public List<AnnotateImageRequest> create(String filePath) throws IOException {
        ByteString imgBytes = fileReader.readFile(filePath);
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);
        return requests;
    }
}
