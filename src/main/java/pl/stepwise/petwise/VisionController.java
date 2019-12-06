package pl.stepwise.petwise;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.stepwise.petwise.response.VisionAnnotation;

import java.util.List;

@RestController
@RequestMapping(value = "api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @GetMapping
    public ResponseEntity<List<VisionAnnotation>> test(@RequestParam("filepath") String filePath) throws Exception {
        return ResponseEntity.ok(visionService.processImage(filePath));
    }
}
