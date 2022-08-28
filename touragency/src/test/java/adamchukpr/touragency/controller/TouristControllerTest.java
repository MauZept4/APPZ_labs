package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TouristDTO;
import adamchukpr.touragency.repos.TouristRepository;
import adamchukpr.touragency.entity.Tourist;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.TouristStub.getTouristRequest;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TouristControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TouristRepository touristRepository;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void successfullyGetAll() throws Exception {
        var tourist1 = getRandomTourist();
        var tourist2 = Tourist.builder()
                            .id(1L)
                            .fullName("Anna")
                            .address("Address")
                            .phone("+380966320145")
                            .email("mail@gmail.com")
                            .build();
        when(touristRepository.findAll()).thenReturn(List.of(tourist1, tourist2));
        mockMvc.perform(getRequest("/tourists"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(tourist1.getFullName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(tourist1.getAddress())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomTourist();
        when(touristRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/tourists/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getFullName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getAddress())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getTouristRequest();
        var expected = getRandomTourist();
        when(touristRepository.findById(any())).thenReturn(Optional.of(getRandomTourist()));
        when(touristRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/tourists", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getFullName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getAddress())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getTouristRequest();
        request.setFullName("Name1");
        var expected = getRandomTourist();
        when(touristRepository.findById(any())).thenReturn(Optional.of(expected));
        when(touristRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/tourists/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getFullName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getAddress())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/tourists/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(touristRepository, atLeast(1)).deleteById(1L);
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, TouristDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, TouristDTO request) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder deleteRequest(String url) {
        return delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}