package pl.stepwise.petwise.pet.service;

import com.google.cloud.storage.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;
import pl.stepwise.petwise.pet.model.LocationRequest;
import pl.stepwise.petwise.pet.model.SpottedRequest;
import pl.stepwise.petwise.pet.model.entity.SpottedReport;
import pl.stepwise.petwise.pet.repository.SpottedReportRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpottedReportService {

    private final SpottedReportRepository spottedReportRepository;
    private final GeometryService geometryService;
    private final GoogleStorageService storageService;

    public SpottedReportService(SpottedReportRepository spottedReportRepository,
                                GeometryService geometryService,
                                GoogleStorageService storageService) {
        this.spottedReportRepository = spottedReportRepository;
        this.geometryService = geometryService;
        this.storageService = storageService;
    }

    public SpottedReport reportSpotted(MultipartFile file, SpottedRequest request) throws FileUploadException {
        Blob blob = storageService.upload(file);
        var notification = SpottedReport.builder()
                .placeOfFinding(request.getLocation())
                .timeOfFinding(LocalDateTime.now())
                .imageId(blob.getName())
                .build();
        return spottedReportRepository.save(notification);
    }

    public List<SpottedReport> findSpottedReportsByLocation(LocationRequest request) {
        var circle = geometryService.createCircle(request.getLocation().getX(), request.getLocation().getY(), request.getRadius());
        return spottedReportRepository.findAllWithin(circle);
    }

}
