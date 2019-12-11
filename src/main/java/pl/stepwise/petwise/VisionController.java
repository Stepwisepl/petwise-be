package pl.stepwise.petwise;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;

import java.util.List;

@RestController
@RequestMapping(value = "api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @GetMapping("labels")
    public ResponseEntity<List<PetwiseLabel>> detectImageLabels(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.detectLabels(filePath));
    }

    @GetMapping("objects")
    public ResponseEntity<List<PetwiseLocalizedObject>> detectObjectLocalization(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.localizeObjects(filePath));
    }

    @GetMapping("crop")
    public ResponseEntity<List<PetwiseCropHint>> getCropHints(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.getCropHints(filePath));
    }
}
