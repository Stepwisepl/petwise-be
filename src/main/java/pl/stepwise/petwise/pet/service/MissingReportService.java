package pl.stepwise.petwise.pet.service;

import org.springframework.stereotype.Service;
import pl.stepwise.petwise.pet.model.LocationRequest;
import pl.stepwise.petwise.pet.model.MissingReportRequest;
import pl.stepwise.petwise.pet.model.MissingReportStatus;
import pl.stepwise.petwise.pet.model.entity.MissingReport;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.repository.MissingReportRepository;
import pl.stepwise.petwise.pet.repository.PetRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MissingReportService {

    private final PetRepository petRepository;
    private final MissingReportRepository reportRepository;
    private final GeometryService geometryService;

    public MissingReportService(PetRepository petRepository,
                                MissingReportRepository reportRepository,
                                GeometryService geometryService) {
        this.petRepository = petRepository;
        this.reportRepository = reportRepository;
        this.geometryService = geometryService;
    }

    public MissingReport reportMissing(MissingReportRequest request) {
        return petRepository.findById(request.getPetId())
                .map(pet -> setReport(request, pet))
                .orElseThrow(EntityNotFoundException::new);
    }

    private MissingReport setReport(MissingReportRequest request, Pet pet) {
        var report = MissingReport.builder()
                .pet(pet)
                .placeOfMissing(request.getPlaceOfMissing())
                .status(MissingReportStatus.REPORTED)
                .reportTime(LocalDateTime.now())
                .build();
        return reportRepository.save(report);
    }

    public List<MissingReport> findAllMissingReportsByLocation(LocationRequest request) {
        var circle = geometryService.createCircle(request.getLocation().getX(), request.getLocation().getY(), request.getRadius());
        return reportRepository.findAllWithin(circle);
    }
}
