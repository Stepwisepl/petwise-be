package pl.stepwise.petwise.file.controller;

import com.google.cloud.storage.Blob;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;

@RestController
@RequestMapping(value = "api/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final GoogleStorageService storageService;

    public FileController(GoogleStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("{fileName}")
    public ResponseEntity<Resource> get(@PathVariable String fileName) {
        Blob blob = storageService.download(fileName);
        var file = blob.getContent();
        return ResponseEntity.ok()
                .contentLength(file.length)
                .contentType(MediaType.parseMediaType(blob.getContentType()))
                .body(new ByteArrayResource(file));
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws FileUploadException {
        return ResponseEntity.ok(storageService.upload(file).getMediaLink());
    }
}
