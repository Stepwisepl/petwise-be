package pl.stepwise.petwise.pet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import pl.stepwise.petwise.pet.model.MissingReportRequest;
import pl.stepwise.petwise.pet.model.MissingReportStatus;
import pl.stepwise.petwise.pet.model.entity.MissingReport;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.service.MissingReportService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MissingReportController.class)
class MissingReportControllerTest {

    private final String apiPath = "/api/missing";
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private JacksonTester<MissingReportRequest> jacksonTester;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MissingReportService serviceMock;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, mapper);
    }

    @Test
    void shouldSaveNewMissingReport() throws Exception {
        //given
        var pet = getPet();
        var request = getRequest();
        var missingReportResponse = getResponse(pet);
        given(serviceMock.reportMissing(request))
                .willReturn(missingReportResponse);
        //when
        MockHttpServletResponse servletResponse = mvc.perform(post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(request).getJson()))
                .andReturn().getResponse();
        //then
        try {
            var response = mapper.readValue(servletResponse.getContentAsString(), MissingReport.class);
            assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
            assertAll("response",
                    () -> assertEquals(pet, response.getPet()),
                    () -> assertEquals(MissingReportStatus.REPORTED, response.getStatus()),
                    () -> assertNull(response.getSpottedReports())
            );
        } catch (UnrecognizedPropertyException e) {
            throw new AssertionError("The response could not be converted into a proper Pet object. " +
                    "The body of the received response=" + servletResponse.getContentAsString() + ".", e);
        }
    }

    private MissingReportRequest getRequest() {
        return MissingReportRequest.builder()
                    .placeOfMissing(null)
                    .petId(1L)
                    .build();
    }

    private Pet getPet() {
        return Pet.builder()
                .id(1L)
                .imageId("1234")
                .name("Mr Cuddles")
                .build();
    }

    private MissingReport getResponse(Pet pet) {
        return MissingReport.builder()
                .placeOfMissing(null)
                .reportTime(LocalDateTime.now())
                .pet(pet)
                .status(MissingReportStatus.REPORTED)
                .build();
    }
}