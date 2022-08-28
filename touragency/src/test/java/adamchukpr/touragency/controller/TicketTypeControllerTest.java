package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TicketTypeDTO;
import adamchukpr.touragency.enums.TypeByComfort;
import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.repos.TicketTypeRepository;
import adamchukpr.touragency.entity.TicketType;
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

import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static adamchukpr.touragency.stub.TicketTypeStub.getTicketTypeRequest;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketTypeRepository ticketTypeRepository;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void successfullyGetAll() throws Exception {
        var ticketType1 = getRandomTicketType();
        var ticketType2 = TicketType.builder()
                                .id(2L)
                                .typeName("Best Ticket")
                                .price(300)
                                .typeByPrice(TypeByPrice.REGULAR)
                                .typeByComfort(TypeByComfort.ECONOMY)
                                .description("description for best ticket")
                                .tickets(new ArrayList<>())
                                .build();
        when(ticketTypeRepository.findAll()).thenReturn(List.of(ticketType1, ticketType2));
        mockMvc.perform(getRequest("/ticket-types"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(ticketType1.getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(ticketType2.getTypeName())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomTicketType();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/ticket-types/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getTypeName())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getTicketTypeRequest();
        var expected = getRandomTicketType();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(getRandomTicketType()));
        when(ticketTypeRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/ticket-types", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTypeName())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getTicketTypeRequest();
        request.setTypeName("Type Name 2");
        var expected = getRandomTicketType();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(expected));
        when(ticketTypeRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/ticket-types/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTypeName())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/ticket-types/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(ticketTypeRepository, atLeast(1)).deleteById(1L);
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, TicketTypeDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, TicketTypeDTO request) {
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