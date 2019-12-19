package pl.stepwise.petwise.pet.service;

import com.google.cloud.storage.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;
import pl.stepwise.petwise.pet.model.MissingReportRequest;
import pl.stepwise.petwise.pet.model.MissingReportStatus;
import pl.stepwise.petwise.pet.model.entity.MissingReport;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.repository.MissingReportRepository;
import pl.stepwise.petwise.pet.repository.PetRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class PetService {

    private final PetRepository repository;
    private final MissingReportRepository reportRepository;
    private final GoogleStorageService storageService;

    public PetService(PetRepository repository,
                      MissingReportRepository reportRepository,
                      GoogleStorageService storageService) {
        this.repository = repository;
        this.reportRepository = reportRepository;
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

    public MissingReport reportMissing(MissingReportRequest request) {
        return repository.findById(request.getPetId())
                .map(pet -> setReport(request, pet))
                .orElseThrow(EntityNotFoundException::new);
    }

    private MissingReport setReport( MissingReportRequest request, Pet pet) {
        var report = MissingReport.builder()
                .pet(pet)
                .placeOfMissing(request.getPlaceOfMissing())
                .status(MissingReportStatus.REPORTED)
                .reportTime(LocalDateTime.now())
        .build();
        return reportRepository.save(report);
    }
}
