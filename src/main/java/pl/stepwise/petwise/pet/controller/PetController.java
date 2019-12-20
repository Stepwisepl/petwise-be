package pl.stepwise.petwise.pet.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.service.PetService;

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

    @GetMapping("{petId}")
    public ResponseEntity<Pet> getById(@PathVariable Long petId) {
        var pet = service.findPetBy(petId);
        return pet.map((p) -> ResponseEntity.ok().body(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{petId}")
    public ResponseEntity<Pet> delete(@PathVariable Long petId) {
        try {
            service.delete(petId);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
