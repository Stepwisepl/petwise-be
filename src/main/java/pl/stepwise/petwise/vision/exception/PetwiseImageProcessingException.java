package pl.stepwise.petwise.vision.exception;

import org.springframework.cloud.gcp.vision.CloudVisionException;

public class PetwiseImageProcessingException extends Exception {

    public PetwiseImageProcessingException(String message, CloudVisionException e) {
        super(message, e);
    }
}
