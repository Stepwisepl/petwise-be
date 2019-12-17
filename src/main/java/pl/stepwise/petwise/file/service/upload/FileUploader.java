package pl.stepwise.petwise.file.service.upload;

import pl.stepwise.petwise.file.exception.FileUploadException;

public interface FileUploader<RETURNS, RECEIVES> {

    RETURNS upload(RECEIVES file) throws FileUploadException;
}
