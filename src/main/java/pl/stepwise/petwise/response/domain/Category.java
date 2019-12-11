package pl.stepwise.petwise.response.domain;

/**
 * This enum helps distinguish objects identified by Vision API as animals.
 * We won't be focusing on other objects identified on a picture, therefore we need to filter only those that represent animals.
 * @todo Adjust categories and map a detected object to a predetermined category (see: https://cloud.google.com/solutions/image-search-app-with-cloud-vision#classifying-images-with-the-vision-api).
 */
public enum Category {
    ANIMALS(true),
    OTHER(false);

    public boolean isValid() {
        return isValid;
    }

    boolean isValid;

    Category(boolean valid) {
        isValid = valid;
    }
}
