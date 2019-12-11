package pl.stepwise.petwise.response;

import org.springframework.stereotype.Service;
import pl.stepwise.petwise.response.domain.Category;

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
