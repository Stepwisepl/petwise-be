package pl.stepwise.petwise.file.service.upload;

import com.google.cloud.storage.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.config.FileUploadConfiguration;
import pl.stepwise.petwise.file.exception.FileServiceException;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.service.FileService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class GoogleStorageService {

    private final FileUploadConfiguration uploadConfig;
    private final FileService fileService;
    private final Storage storage;

    public GoogleStorageService(FileUploadConfiguration uploadConfig,
                                FileService fileService,
                                Storage storage) {
        this.uploadConfig = uploadConfig;
        this.fileService = fileService;
        this.storage = storage;
    }

    public Blob upload(MultipartFile file) throws FileUploadException {
        try {
            BlobInfo blobInfo = getBlobInfo(fileService.getFileName(file), file.getContentType());
            return storage.create(blobInfo, file.getInputStream().readAllBytes());
        } catch (FileServiceException | StorageException | IOException e) {
            throw new FileUploadException("File upload failed with the following exception:", e);
        }
    }

    private BlobInfo getBlobInfo(String fileName, String contentType) {
        BlobId blobId = BlobId.of(uploadConfig.getBucketName(), UUID.randomUUID().toString());
        List<Acl> acl = (Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER)));
        if (contentType == null) {
            log.warn("Content type of file: " + fileName + " could not be established.");
            contentType = "image/*";
        }
        return BlobInfo.newBuilder(blobId).setContentType(contentType).setAcl(acl).build();
    }

    public Blob getFile(String fileId) {
        BlobId blobId = BlobId.of(uploadConfig.getBucketName(), fileId);
        return storage.get(blobId);
    }
}
