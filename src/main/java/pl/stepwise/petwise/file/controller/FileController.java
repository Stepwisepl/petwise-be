package pl.stepwise.petwise.file.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageUploader;

@RestController
@RequestMapping(value = "api/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final GoogleStorageUploader uploader;

    public FileController(GoogleStorageUploader uploader) {
        this.uploader = uploader;
    }

    @PostMapping("upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(uploader.upload(file).getMediaLink());
        } catch (FileUploadException e) {
            return ResponseEntity.ok("error");
        }
    }
}
