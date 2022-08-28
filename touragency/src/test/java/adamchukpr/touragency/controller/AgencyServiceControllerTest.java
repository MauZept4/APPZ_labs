package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.AgencyServiceDTO;
import adamchukpr.touragency.entity.AgencyService;
import adamchukpr.touragency.repos.AgencyServiceRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static adamchukpr.touragency.stub.AgencyServiceStub.getAgencyServiceRequest;
import static adamchukpr.touragency.stub.AgencyServiceStub.getRandomAgencyService;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgencyServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgencyServiceRepository agencyServiceRepository;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void successfullyGetAll() throws Exception {
        var agencyService1 = getRandomAgencyService();
        var agencyService2 = AgencyService.builder()
                                        .id(1L)
                                        .serviceName("Consulting")
                                        .description("Consul tourist on his possible trips")
                                        .price(700L)
                                        .agencyServiceOrders(new ArrayList<>())
                                        .build();
        when(agencyServiceRepository.findAll()).thenReturn(List.of(agencyService1, agencyService2));
        mockMvc.perform(getRequest("/agency-services"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(agencyService1.getServiceName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(agencyService1.getDescription())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomAgencyService();
        when(agencyServiceRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/agency-services/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getServiceName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getAgencyServiceRequest();
        var expected = getRandomAgencyService();
        when(agencyServiceRepository.findById(any())).thenReturn(Optional.of(getRandomAgencyService()));
        when(agencyServiceRepository.save(any())).thenReturn(expected);
        mockMvc.perform(agencyServiceRequest("/agency-services", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getServiceName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getAgencyServiceRequest();
        request.setServiceName("ServiceName1");
        var expected = getRandomAgencyService();
        when(agencyServiceRepository.findById(any())).thenReturn(Optional.of(expected));
        when(agencyServiceRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/agency-services/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getServiceName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/agency-services/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(agencyServiceRepository, atLeast(1)).deleteById(1L);
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, AgencyServiceDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder agencyServiceRequest(String url, AgencyServiceDTO request) {
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