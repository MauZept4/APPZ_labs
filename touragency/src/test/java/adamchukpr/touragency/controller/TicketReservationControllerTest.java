package adamchukpr.touragency.controller;

import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.dto.TicketReservationDTO;
import adamchukpr.touragency.repos.TicketRepository;
import adamchukpr.touragency.repos.TicketReservationRepository;
import adamchukpr.touragency.repos.TicketTypeRepository;
import adamchukpr.touragency.repos.TouristRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.TicketReservationStub.getTicketReservationRequest;
import static adamchukpr.touragency.stub.TicketReservationStub.getRandomTicketReservation;
import static adamchukpr.touragency.stub.TicketStub.getRandomTicket;
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketReservationRepository ticketReservationRepository;

    @MockBean
    private TouristRepository touristRepository;

    @MockBean
    private TicketTypeRepository ticketTypeRepository;

    @MockBean
    private TicketRepository ticketRepository;


    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void successfullyGetAll() throws Exception {
        var ticketReservation1 = getRandomTicketReservation();
        var ticketReservation2 = TicketReservation.builder()
                                              .id(2L)
                                              .tourist(getRandomTourist())
                                              .employee(getRandomEmployee())
                                              .ticket(getRandomTicket())
                                              .totalPrice(4000L)
                                              .startDate(LocalDate.of(2022, 1, 1))
                                              .finishDate(LocalDate.of(2022, 1, 4))
                                              .build();
        when(ticketReservationRepository.findAll()).thenReturn(List.of(ticketReservation1, ticketReservation2));
        mockMvc.perform(getRequest("/ticket-reservations"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(ticketReservation1.getTicket()
                                                                                      .getTicketType()
                                                                                      .getDescription())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(ticketReservation2.getTicket()
                                                                                                     .getTicketNumber()))));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomTicketReservation();
        when(ticketReservationRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/ticket-reservations/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getTicket()
                                                                                     .getTicketType()
                                                                                     .getDescription())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getTicket().getTicketNumber())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getTicketReservationRequest();
        var expected = getRandomTicketReservation();
        var ticketType = getRandomTicketType();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketType));
        when(ticketReservationRepository.findById(any())).thenReturn(Optional.of(getRandomTicketReservation()));
        when(ticketReservationRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/ticket-reservations", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTicket()
                                                                              .getTicketType()
                                                                              .getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTicket().getTicketNumber())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getTicketReservationRequest();
        var tourist = Tourist.builder().id(2L).fullName("Maria Shevchuk").build();
        var ticket = getRandomTicket();
        var ticketType = getRandomTicketType();
        request.setTouristId(2L);
        request.setTicketId(1L);
        var expected = getRandomTicketReservation();
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketType));
        when(touristRepository.findById(any())).thenReturn(Optional.of(tourist));
        when(ticketReservationRepository.findById(any())).thenReturn(Optional.of(expected));
        when(ticketReservationRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/ticket-reservations/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTicket()
                                                                              .getTicketType()
                                                                              .getDescription())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getTourist().getFullName())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/ticket-reservations/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(ticketReservationRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void successfullyGetByTicket() throws Exception {
        var ticketReservation1 = getRandomTicketReservation();
        var ticketReservation2 = TicketReservation.builder()
                                              .id(2L)
                                              .tourist(getRandomTourist())
                                              .employee(getRandomEmployee())
                                              .ticket(getRandomTicket())
                                              .totalPrice(5000L)
                                              .startDate(LocalDate.of(2021, 3, 3))
                                              .finishDate(LocalDate.of(2021, 2, 7))
                                              .build();

        ticketReservation1.setTicket(getRandomTicket());
        ticketReservation2.setTicket(getRandomTicket());

        var expected = List.of(ticketReservation1, ticketReservation2);
        when(ticketReservationRepository.findAllReservationsByTicketId(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/ticket-reservations/by-ticket-id/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0).getTicket().getTicketNumber())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(expected.get(0)
                                                                                             .getTotalPrice()))))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1)
                                                                              .getTicket().getTicketNumber())));
    }

    @Test
    void successfullyGetByTourist() throws Exception {
        var ticketReservation1 = getRandomTicketReservation();
        var ticketReservation2 = TicketReservation.builder()
                                              .id(2L)
                                              .tourist(getRandomTourist())
                                              .employee(getRandomEmployee())
                                              .ticket(getRandomTicket())
                                              .totalPrice(5000L)
                                              .startDate(LocalDate.of(2021, 3, 3))
                                              .finishDate(LocalDate.of(2021, 2, 7))
                                              .build();

        ticketReservation1.setTourist(getRandomTourist());
        ticketReservation2.setTourist(getRandomTourist());

        var expected = List.of(ticketReservation1, ticketReservation2);
        when(ticketReservationRepository.findAllReservationsByTouristId(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/ticket-reservations/by-tourist-id/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0).getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(expected.get(0)
                                                                                             .getTotalPrice()))))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1)
                                                                              .getTourist().getFullName())));
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, TicketReservationDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, TicketReservationDTO request) {
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