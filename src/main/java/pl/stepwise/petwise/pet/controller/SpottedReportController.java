package pl.stepwise.petwise.pet.controller;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.pet.model.LocationRequest;
import pl.stepwise.petwise.pet.model.SpottedRequest;
import pl.stepwise.petwise.pet.model.entity.SpottedReport;
import pl.stepwise.petwise.pet.service.SpottedReportService;

import java.util.List;

@RestController
@RequestMapping(value = "api/spotted", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpottedReportController {

    private final SpottedReportService service;

    public SpottedReportController(SpottedReportService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SpottedReport> reportSpotted(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("coordinate1") double coordinate1,
                                                       @RequestParam("coordinate2") double coordinate2) throws FileUploadException {
        GeometryFactory gf = new GeometryFactory();
        var request = SpottedRequest.builder()
                .location(gf.createPoint(new Coordinate(coordinate1, coordinate2)))
                .build();
        return ResponseEntity.ok(service.reportSpotted(file, request));
    }

    @GetMapping("within-radius")
    public ResponseEntity<List<SpottedReport>> getNotificationsNearLocation(@RequestBody LocationRequest request) {
        return ResponseEntity.ok(service.findSpottedReportsByLocation(request));
    }
}
