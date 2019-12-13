package pl.stepwise.petwise.vision.service;

import org.springframework.stereotype.Service;
import pl.stepwise.petwise.vision.model.Category;

@Service
public class LocalizedObjectClassifier {

    public Category assignCategory(String name) {
        if (name.equals("Dog")) {
            return Category.ANIMALS;
        } else {
            return Category.OTHER;
        }
    }
}
