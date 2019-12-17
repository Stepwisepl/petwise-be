package pl.stepwise.petwise.file.model;

import java.util.Arrays;

public enum AvailableFileType {

    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");

    private String extension;

    AvailableFileType(String extension) {
        this.extension = extension;
    }

    public static boolean hasValidExtension(String extension) {
        return Arrays.stream(AvailableFileType.values())
                .anyMatch(v -> v.getExtension().equals(extension));
    }

    public String getExtension() {
        return this.extension;
    }
}
