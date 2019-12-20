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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.pet.model.entity.Pet;
import pl.stepwise.petwise.pet.service.PetService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = PetController.class)
class PetControllerTest {

    private final String apiPath = "/api/pet";
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private JacksonTester<Pet> petJacksonTester;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService serviceMock;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, mapper);
    }

    @Test
    void shouldSaveNewPet() throws Exception {
        //given
        given(serviceMock.save(any(MultipartFile.class), eq("Mr Cuddles")))
                .willReturn(getPet());
        var fileMock = mock(MultipartFile.class);
        var imageBytes = new byte[1];
        given(fileMock.getBytes())
                .willReturn(imageBytes);
        //when
        MockHttpServletResponse servletResponse = mvc.perform(multipart(apiPath).file("file", fileMock.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .queryParam("name", "Mr Cuddles"))
                .andReturn().getResponse();
        //then
        try {
            var response = mapper.readValue(servletResponse.getContentAsString(), Pet.class);
            assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
            assertAll("response",
                    () -> assertEquals("Mr Cuddles", response.getName()),
                    () -> assertEquals("1234", response.getImageId()),
                    () -> assertNull(response.getMissingReport())
            );
        } catch (UnrecognizedPropertyException e) {
            throw new AssertionError("The response could not be converted into a proper Pet object. " +
                    "The body of the received response=" + servletResponse.getContentAsString() + ".", e);
        }
    }

    private Pet getPet() {
        return Pet.builder()
                .id(1L)
                .imageId("1234")
                .name("Mr Cuddles")
                .build();
    }

    @Test
    void shouldGetPetById() throws Exception {
        //given
        given(serviceMock.findPetBy(1L))
                .willReturn(Optional.of(getPet()));
        //when
        MockHttpServletResponse servletResponse = mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        try {
            var response = mapper.readValue(servletResponse.getContentAsString(), Pet.class);
            assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
            assertAll("response",
                    () -> assertEquals("Mr Cuddles", response.getName()),
                    () -> assertEquals("1234", response.getImageId()),
                    () -> assertNull(response.getMissingReport())
            );
        } catch (UnrecognizedPropertyException e) {
            throw new AssertionError("The response could not be converted into a proper Pet object. " +
                    "The body of the received response=" + servletResponse.getContentAsString() + ".", e);
        }
    }

    @Test
    void shouldNotFoundDeletedPet() throws Exception {
        willThrow(EmptyResultDataAccessException.class)
                .given(serviceMock)
                .delete(1L);

        mvc.perform(delete(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}