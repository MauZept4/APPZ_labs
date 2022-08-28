package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TicketDTO;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.repos.TicketRepository;
import adamchukpr.touragency.repos.TicketTypeRepository;
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

import static adamchukpr.touragency.stub.TicketStub.getTicketRequest;
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static adamchukpr.touragency.stub.TicketStub.getRandomTicket;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketRepository ticketRepository;

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
        var ticket1 = getRandomTicket();
        var ticket2 = Ticket.builder()
                        .id(2L)
                        .ticketNumber("45B")
                        .isAvailable(true)
                        .ticketType(getRandomTicketType())
                        .TicketReservations(new ArrayList<>())
                        .build();
        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2));
        mockMvc.perform(getRequest("/tickets"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(ticket1.getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(ticket2.getTicketType().getDescription())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomTicket();
        when(ticketRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/tickets/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getTicketType().getDescription())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getTicketRequest();
        var expected = getRandomTicket();
        when(ticketRepository.findById(any())).thenReturn(Optional.of(getRandomTicket()));
        when(ticketRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/tickets", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTicketType().getDescription())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getTicketRequest();
        request.setTicketNumber("S54");
        request.setTicketTypeId(1L);
        var expected = getRandomTicket();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(getRandomTicketType()));
        when(ticketRepository.findById(any())).thenReturn(Optional.of(expected));
        when(ticketRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/tickets/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTicketType().getDescription())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/tickets/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(ticketRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void successfullyGetAllAvailable() throws Exception {
        var ticket1 = getRandomTicket();
        var ticket2 = getRandomTicket();
        ticket2.setId(2L);
        ticket1.setAvailable(true);
        ticket2.setAvailable(true);
        var expected = List.of(ticket1, ticket2);
        when(ticketRepository.findAllAvailable(any())).thenReturn(expected);
        mockMvc.perform(getRequest("/tickets/available"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.get(0).getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1).getTicketType().getDescription())));

    }

    @Test
    void successfullyGetAllNotAvailable() throws Exception {
        var ticket1 = getRandomTicket();
        var ticket2 = getRandomTicket();
        ticket2.setId(2L);
        ticket1.setAvailable(false);
        ticket2.setAvailable(false);
        var expected = List.of(ticket1, ticket2);
        when(ticketRepository.findAllNotAvailable(any())).thenReturn(expected);
        mockMvc.perform(getRequest("/tickets/not-available"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.get(0).getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1).getTicketType().getDescription())));
    }

    @Test
    void successfullyGetByTicketType() throws Exception {
        var ticket1 = getRandomTicket();
        var ticket2 = Ticket.builder()
                        .id(2L)
                        .ticketNumber("31F")
                        .isAvailable(true)
                        .ticketType(getRandomTicketType())
                        .TicketReservations(new ArrayList<>())
                        .build();

        ticket1.setTicketType(getRandomTicketType());
        ticket2.setTicketType(getRandomTicketType());

        var expected = List.of(ticket1, ticket2);
        when(ticketRepository.findAllByTicketType_id(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/tickets/ticket-type/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getTicketType()
                                                                              .getDescription())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getTicketType().getTypeName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(expected.get(1)
                                                                                             .getTicketType()
                                                                                             .getPrice()))));
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, TicketDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, TicketDTO request) {
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