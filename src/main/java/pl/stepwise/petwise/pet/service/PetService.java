package pl.stepwise.petwise.pet.service;

import com.google.cloud.storage.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.repository.PetRepository;

import java.util.Optional;

@Service
public class PetService {

    private final PetRepository repository;
    private final GoogleStorageService storageService;

    public PetService(PetRepository repository,
                      GoogleStorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    public Pet save(MultipartFile file, String name) throws FileUploadException {
        Blob blob = storageService.upload(file);
        var pet = Pet.builder()
                .name(name)
                .imageId(blob.getName())
                .build();
        return repository.save(pet);
    }

    public Optional<Pet> findPetBy(Long id) {
        return repository.findById(id);
    }

    public void delete(Long petId) {
        repository.deleteById(petId);
    }
}
