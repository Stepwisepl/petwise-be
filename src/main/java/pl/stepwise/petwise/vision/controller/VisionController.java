package pl.stepwise.petwise.vision.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.stepwise.petwise.vision.exception.InvalidLocalizedObjectAmountException;
import pl.stepwise.petwise.vision.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.vision.model.PetwiseCropHint;
import pl.stepwise.petwise.vision.model.PetwiseLabel;
import pl.stepwise.petwise.vision.model.localizedobject.PetwiseLocalizedObject;
import pl.stepwise.petwise.vision.service.VisionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "api/vision", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @GetMapping("labels")
    public ResponseEntity<List<PetwiseLabel>> detectImageLabels(@RequestParam("fileid") String fileId) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.detectLabels(fileId));
    }

    @GetMapping("labels-after-crop")
    public ResponseEntity<List<PetwiseLabel>> detectImageLabelsAfterCrop(@RequestParam("fileid") String fileId) throws PetwiseImageProcessingException, IOException, InvalidLocalizedObjectAmountException {
        return ResponseEntity.ok(visionService.detectLabelsAfterCroppingObject(fileId));
    }

    @GetMapping("objects")
    public ResponseEntity<List<PetwiseLocalizedObject>> detectObjectLocalization(@RequestParam("fileid") String fileId) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.localizeObjects(fileId));
    }

    @GetMapping("eligible-object")
    public ResponseEntity<PetwiseLocalizedObject> detectEligibleObjectLocalization(@RequestParam("fileid") String fileId) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.getEligibleObject(fileId));
    }

    @GetMapping("crop")
    public ResponseEntity<List<PetwiseCropHint>> getCropHints(@RequestParam("fileid") String fileId) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.getCropHints(fileId));
    }
}
