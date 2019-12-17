package pl.stepwise.petwise.file.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private FileService service;

    @Mock
    private MultipartFile fileMock;

    @BeforeEach
    void setUp() {
        service = new FileService();
    }

    @Test
    void shouldReturnOriginalFileName() throws FileServiceException {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn("file.png");
        //when
        String fileName = service.getFileName(fileMock);
        //then
        assertAll("file",
                () -> assertNotNull(fileName),
                () -> assertEquals("file.png", fileName)
                );
    }

    @Test
    void shouldThrowExceptionWhenFileNameIsNull() {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn(null);
        //then
        assertThrows(FileServiceException.class, () -> service.getFileName(fileMock));
    }

    @Test
    void shouldThrowExceptionWhenInvalidFileExtension() {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn("file.invalid");
        //then
        assertThrows(FileServiceException.class, () -> service.getFileName(fileMock));
    }

    @Test
    void shouldThrowExtensionWhenFileWithoutExtension() {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn("file");
        //then
        assertThrows(FileServiceException.class, () -> service.getFileName(fileMock));
    }

    @Test
    void shouldThrowExtensionWhenFileWithoutExtensionButWithDotInTheEnd() {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn("file.");
        //then
        assertThrows(FileServiceException.class, () -> service.getFileName(fileMock));
    }

    @Test
    void shouldThrowExtensionWhenFileWithoutExtensionButWithDotInTheBeginning() {
        //given
        given(fileMock.getOriginalFilename())
                .willReturn(".file");
        //then
        assertThrows(FileServiceException.class, () -> service.getFileName(fileMock));
    }
}