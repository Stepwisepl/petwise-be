package pl.stepwise.petwise.file.service.upload;

import com.google.cloud.storage.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.config.FileUploadConfiguration;
import pl.stepwise.petwise.file.exception.FileServiceException;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.exception.StorageProviderException;
import pl.stepwise.petwise.file.service.FileService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
public class GoogleStorageUploader implements FileUploader<Blob, MultipartFile> {

    private final FileUploadConfiguration uploadConfig;
    private final FileService fileService;
    private final StorageProvider storageProvider;

    public GoogleStorageUploader(FileUploadConfiguration uploadConfig,
                                 FileService fileService,
                                 StorageProvider storageProvider) {
        this.uploadConfig = uploadConfig;
        this.fileService = fileService;
        this.storageProvider = storageProvider;
    }

    public Blob upload(MultipartFile file) throws FileUploadException {
        try {
            BlobInfo blobInfo = getBlobInfo(fileService.getFileName(file), file.getContentType());
            Storage storage = storageProvider.get();
            return storage.create(blobInfo, file.getInputStream().readAllBytes());
        } catch (FileServiceException | StorageException | StorageProviderException | IOException e) {
            throw new FileUploadException("File upload failed with the following exception:", e);
        }
    }

    private BlobInfo getBlobInfo(String fileName, String contentType) {
        BlobId blobId = BlobId.of(uploadConfig.getBucketName(), fileName);
        List<Acl> acl = (Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER)));
        if (contentType == null) {
            log.warn("Content type of file: " + fileName + " could not be established.");
            contentType = "image/*";
        }
        return BlobInfo.newBuilder(blobId).setContentType(contentType).setAcl(acl).build();
    }
}
