package pl.stepwise.petwise;

import org.junit.jupiter.api.Test;
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
    void test() throws Exception {
        String dirPath = "/api/test";
        mvc.perform(get(dirPath + "?filepath=test-pictures/dog1.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
        mvc.perform(get(dirPath + "?filepath=test-pictures/dog2.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chair")))
                .andDo(print());
        mvc.perform(get(dirPath + "?filepath=test-pictures/dog3.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beagle")))
                .andDo(print());
    }
}