package pl.stepwise.petwise.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileServiceException;
import pl.stepwise.petwise.file.model.AvailableFileType;

import java.util.Optional;

@Service
public class FileService {

    public String getFileName(MultipartFile file) throws FileServiceException {
        var fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new FileServiceException("File name can't be empty.");
        }
        if (!AvailableFileType.hasValidExtension(getExtension(fileName))) {
            throw new FileServiceException("File extension in file " + fileName + " is not valid.");
        }
        return fileName;
    }

    private String getExtension(String fileName) throws FileServiceException {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .orElseThrow(() -> new FileServiceException("File extension could not be found in file " + fileName));
    }
}
