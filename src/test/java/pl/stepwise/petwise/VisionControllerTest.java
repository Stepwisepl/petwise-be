package pl.stepwise.petwise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    @Timeout(3)
    void shouldGetLabelsForClearImage() throws Exception {
        String route = "/api/test/labels";
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
//    @Timeout(3)
    void shouldGetLabelsForAutomaticallyCroppedImage() throws Exception {
        String route = "/api/test/crop-labels";
        mvc.perform(get(route + "?filepath=test-pictures/dog3.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldRecognizeDogBreedInProperlyCroppedImage() throws Exception {
        String route = "/api/test/labels";
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
        String route = "/api/test/objects";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dog")))
                .andExpect(content().string(containsString("Pillow")))
                .andDo(print());
    }

    @Test
    @Timeout(3)
    void shouldGetCropHintsForGivenImage() throws Exception {
        String route = "/api/test/crop";
        mvc.perform(get(route + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk());
    }
}