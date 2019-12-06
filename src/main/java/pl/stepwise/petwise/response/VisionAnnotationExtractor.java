package pl.stepwise.petwise.response;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class VisionAnnotationExtractor {

    public List<VisionAnnotation> extract(List<AnnotateImageResponse> responses) {
        return responses.stream()
                .flatMap(r -> {
                    if (r.hasError()) {
                        log.debug(r.getError().getMessage());
                        return Stream.empty();
                    }
                    return r.getLabelAnnotationsList().stream()
                            .map(a -> (VisionAnnotation.builder()
                                    .description(a.getDescription())
                                    .mid(a.getMid())
                                    .topicality(a.getTopicality())
                                    .score(a.getScore())
                                    .build()));
                }).collect(Collectors.toList());
    }
}
