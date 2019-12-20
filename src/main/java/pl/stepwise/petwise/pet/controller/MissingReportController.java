package pl.stepwise.petwise.pet.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.stepwise.petwise.pet.model.LocationRequest;
import pl.stepwise.petwise.pet.model.MissingReportRequest;
import pl.stepwise.petwise.pet.model.entity.MissingReport;
import pl.stepwise.petwise.pet.service.MissingReportService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/missing", produces = MediaType.APPLICATION_JSON_VALUE)
public class MissingReportController {

    private final MissingReportService service;

    public MissingReportController(MissingReportService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MissingReport> reportMissing(@Valid @RequestBody MissingReportRequest request) {
        return ResponseEntity.ok(service.reportMissing(request));
    }

    @GetMapping("within-radius")
    public ResponseEntity<List<MissingReport>> getMissingReportsNearLocation(@RequestBody LocationRequest request) {
        return ResponseEntity.ok(service.findAllMissingReportsByLocation(request));
    }
}
