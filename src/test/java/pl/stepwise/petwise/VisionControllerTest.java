package pl.stepwise.petwise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.stepwise.petwise.vision.controller.VisionController;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VisionControllerTest {

    @Autowired
    VisionController controller;

    @Autowired
    private MockMvc mvc;

    private static String API_PATH = "/api/vision/";

    @Test
    @Timeout(4)
    void shouldGetLabelsForClearImage() throws Exception {
        String route = API_PATH + "labels";
        mvc.perform(get(route + "?filepath=test-pictures/dog1.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
        mvc.perform(get(route + "?filepath=test-pictures/dog3.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
    }

    @Test
    @Timeout(5)
    void shouldGetLabelsForAutomaticallyCroppedImage() throws Exception {
        String route = API_PATH + "labels-after-crop";
        mvc.perform(get(route + "?filepath=test-pictures/dog3.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldRecognizeDogBreedInProperlyCroppedImage() throws Exception {
        String route = API_PATH + "labels";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chair")))
                .andDo(print());
        mvc.perform(get(route + "?filepath=test-pictures/dog2-cropped.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Retriever")))
                .andDo(print());
        mvc.perform(get(route + "?filepath=test-pictures/dog2-cropped-properly.png"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldGetObjectsForGivenImage() throws Exception {
        String route = API_PATH + "objects";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dog")))
                .andExpect(content().string(containsString("Pillow")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldGetEligibleObjectForGivenImage() throws Exception {
        String route = API_PATH + "eligible-object";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dog")))
                .andExpect(content().string(containsString("ANIMALS")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldGetCropHintsForGivenImage() throws Exception {
        String route = API_PATH + "crop";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk());
    }
}