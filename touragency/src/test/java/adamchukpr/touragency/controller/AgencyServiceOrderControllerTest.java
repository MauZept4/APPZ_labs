package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.AgencyServiceOrderDTO;
import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.entity.AgencyServiceOrder;
import adamchukpr.touragency.repos.AgencyServiceOrderRepository;
import adamchukpr.touragency.repos.AgencyServiceRepository;
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
import static adamchukpr.touragency.stub.AgencyServiceOrderStub.getRandomAgencyServiceOrder;
import static adamchukpr.touragency.stub.AgencyServiceOrderStub.getAgencyServiceOrderRequest;
import static adamchukpr.touragency.stub.AgencyServiceStub.getRandomAgencyService;
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgencyServiceOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgencyServiceOrderRepository agencyServiceOrderRepository;

    @MockBean
    private TouristRepository touristRepository;

    @MockBean
    private TicketTypeRepository ticketTypeRepository;

    @MockBean
    private AgencyServiceRepository agencyServiceRepository;


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
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(2L)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        when(agencyServiceOrderRepository.findAll()).thenReturn(List.of(agencyServiceOrder1, agencyServiceOrder2));
        mockMvc.perform(getRequest("/agency-service-orders"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(agencyServiceOrder1.getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(agencyServiceOrder2
                                                       .getAgencyService()
                                                       .getServiceName()))));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomAgencyServiceOrder();
        when(agencyServiceOrderRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/agency-service-orders/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getAgencyService()
                                                                                     .getServiceName())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getAgencyServiceOrderRequest();
        var expected = getRandomAgencyServiceOrder();
        var ticketType = getRandomTicketType();
        when(ticketTypeRepository.findById(any())).thenReturn(Optional.of(ticketType));
        when(agencyServiceOrderRepository.findById(any())).thenReturn(Optional.of(getRandomAgencyServiceOrder()));
        when(agencyServiceOrderRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/agency-service-orders", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getAgencyService()
                                                                                        .getServiceName())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getAgencyServiceOrderRequest();
        var tourist = Tourist.builder().id(2L).fullName("Maria Shevchuk").build();
        var agencyService = getRandomAgencyService();
        request.setTouristId(2L);
        request.setAgencyServiceId(1L);
        var expected = getRandomAgencyServiceOrder();
        when(touristRepository.findById(any())).thenReturn(Optional.of(tourist));
        when(agencyServiceRepository.findById(any())).thenReturn(Optional.of(agencyService));
        when(agencyServiceOrderRepository.findById(any())).thenReturn(Optional.of(expected));
        when(agencyServiceOrderRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/agency-service-orders/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getAgencyService().getServiceName())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/agency-service-orders/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(agencyServiceOrderRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void successfullyGetByTourist() throws Exception {
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(2L)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        agencyServiceOrder1.setTourist(getRandomTourist());
        agencyServiceOrder2.setTourist(getRandomTourist());

        var expected = List.of(agencyServiceOrder1, agencyServiceOrder2);
        when(agencyServiceOrderRepository.findAllByTourist_Id(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/agency-service-orders/tourist/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0).getTourist().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getTourist().getEmail())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1)
                                                                              .getEmployee().getFullName())));
    }

    @Test
    void successfullyGetByEmployee() throws Exception {
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(2L)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        agencyServiceOrder1.setEmployee(getRandomEmployee());
        agencyServiceOrder2.setEmployee(getRandomEmployee());

        var expected = List.of(agencyServiceOrder1, agencyServiceOrder2);
        when(agencyServiceOrderRepository.findAllByEmployee_Id(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/agency-service-orders/employee/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0).getEmployee().getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getEmployee().getPhone())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(1)
                                                                              .getEmployee().getFullName())));
    }

    @Test
    void successfullyGetByService() throws Exception {
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(2L)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        agencyServiceOrder1.setAgencyService(getRandomAgencyService());
        agencyServiceOrder2.setAgencyService(getRandomAgencyService());

        var expected = List.of(agencyServiceOrder1, agencyServiceOrder2);
        when(agencyServiceOrderRepository.findAllByAgencyService_Id(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/agency-service-orders/service/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getAgencyService()
                                                                              .getServiceName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getAgencyService().getDescription())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(expected.get(1)
                                                                                             .getAgencyService()
                                                                                             .getPrice()))));
    }

    @Test
    void successfullyGetByDate() throws Exception {
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(2L)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        agencyServiceOrder1.setAgencyService(getRandomAgencyService());
        agencyServiceOrder2.setAgencyService(getRandomAgencyService());

        var expected = List.of(agencyServiceOrder1, agencyServiceOrder2);
        when(agencyServiceOrderRepository.findAllByDate(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/agency-service-orders/date/2022-05-18"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getAgencyService()
                                                                              .getServiceName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getAgencyService().getDescription())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(String.valueOf(expected.get(1)
                                                                                             .getAgencyService()
                                                                                             .getPrice()))));
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, AgencyServiceOrderDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, AgencyServiceOrderDTO request) {
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