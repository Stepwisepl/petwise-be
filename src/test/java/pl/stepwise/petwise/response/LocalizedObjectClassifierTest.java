package pl.stepwise.petwise.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.stepwise.petwise.response.domain.Category;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocalizedObjectClassifierTest {

    private LocalizedObjectClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new LocalizedObjectClassifier();
    }

    @Test
    void shouldClassifyDogAsAnimal() {
        //when
        Category category = classifier.assignCategory("Dog");
        //then
        assertEquals(Category.ANIMALS, category);
    }

    @Test
    void shouldClassifyChairAsOther() {
        //when
        Category category = classifier.assignCategory("Chair");
        //then
        assertEquals(Category.OTHER, category);
    }
}