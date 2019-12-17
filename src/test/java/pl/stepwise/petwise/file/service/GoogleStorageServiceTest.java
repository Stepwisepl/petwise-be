package pl.stepwise.petwise.file.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.config.FileUploadConfiguration;
import pl.stepwise.petwise.file.exception.FileServiceException;
import pl.stepwise.petwise.file.exception.FileUploadException;
import pl.stepwise.petwise.file.exception.StorageProviderException;
import pl.stepwise.petwise.file.service.upload.GoogleStorageService;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GoogleStorageServiceTest {

    private GoogleStorageService storageService;

    @Mock
    private FileUploadConfiguration uploadConfigurationMock;

    @Mock
    private FileService fileServiceMock;

    @Mock
    private Storage storageMock;

    @Mock
    private Blob blobMock;

    @Mock
    private MultipartFile fileMock;

    @BeforeEach
    void setUp() {
        storageService = new GoogleStorageService(uploadConfigurationMock, fileServiceMock, storageMock);
        given(uploadConfigurationMock.getBucketName())
                .willReturn("bucket");
    }

    @Test
    void shouldCallAllFunctionsRequiredToFinalizeFileUpload() throws FileUploadException, FileServiceException, IOException, StorageProviderException {
        //given
        given(fileServiceMock.getFileName(fileMock))
                .willReturn("file.png");
        byte[] imageBytes = getBytes();
        given(storageMock.create(any(), eq(imageBytes)))
                .willReturn(blobMock);
        //when
        Blob blob = storageService.upload(fileMock);
        //then
        assertNotNull(blob);
    }

    private byte[] getBytes() throws IOException {
        var inputStreamMock = mock(InputStream.class);
        given(fileMock.getInputStream())
                .willReturn(inputStreamMock);
        var imageBytes = new byte[1];
        given(inputStreamMock.readAllBytes())
                .willReturn(imageBytes);
        return imageBytes;
    }

    @Test
    void shouldCallAllFunctionsRequiredToFinalizeFileDownload() {
        //given
        given(storageMock.get(any(BlobId.class)))
                .willReturn(blobMock);
        //when
        Blob blob = storageService.getFile("1234567");
        assertNotNull(blob);
    }
}