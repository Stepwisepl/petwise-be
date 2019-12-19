package pl.stepwise.petwise.pet.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.pet.model.MissingReportRequest;
import pl.stepwise.petwise.pet.model.entity.MissingReport;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.service.PetService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/pet", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pet> save(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws FileUploadException {
        return ResponseEntity.ok(service.save(file, name));
    }

    @PostMapping("missing")
    public ResponseEntity<MissingReport> reportMissing(@Valid @RequestBody MissingReportRequest request) {
        return ResponseEntity.ok(service.reportMissing(request));
    }


}
