package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.EmployeeDTO;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.repos.EmployeeRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static adamchukpr.touragency.stub.EmployeeStub.getEmployeeRequest;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.PostStub.getRandomPost;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

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
        var employee1 = getRandomEmployee();
        var employee2 = Employee.builder()
                                .id(2L)
                                .fullName("Olexandr")
                                .phone("+384569874521")
                                .post(getRandomPost())
                                .startDate(LocalDate.of(2022, 1, 3))
                                .endDate(LocalDate.of(2022, 4, 3))
                                .agencyServiceOrder(new ArrayList<>())
                                .build();
        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        mockMvc.perform(getRequest("/employees"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(employee1.getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(employee1.getPost().getPositionName())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomEmployee();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/employees/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expectedProduct.getPost()
                                                                                     .getPositionName())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getEmployeeRequest();
        var expected = getRandomEmployee();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(getRandomEmployee()));
        when(employeeRepository.save(any())).thenReturn(expected);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(postRequest("/employees", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getPost().getPositionName())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getEmployeeRequest();
        request.setFullName("Name1");
        var expected = getRandomEmployee();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(expected));
        when(employeeRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/employees/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.getPhone())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/employees/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(employeeRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void successfullyGetByPosition() throws Exception {
        var employee1 = getRandomEmployee();
        var employee2 = Employee.builder()
                                .id(2L)
                                .fullName("Olexandr")
                                .phone("+384569874521")
                                .post(getRandomPost())
                                .startDate(LocalDate.of(2022, 1, 3))
                                .endDate(LocalDate.of(2022, 4, 3))
                                .agencyServiceOrder(new ArrayList<>())
                                .build();

        employee1.setPost(getRandomPost());
        employee2.setPost(getRandomPost());

        var expected = List.of(employee1);
        when(employeeRepository.findAllEmployeesByPostId(any(), any())).thenReturn(expected);
        mockMvc.perform(getRequest("/employees/position/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.get(0).getFullName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getPost()
                                                                              .getPositionName())))
               .andExpect(MockMvcResultMatchers.content()
                                               .string(containsString(expected.get(0)
                                                                              .getPhone())));
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, EmployeeDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, EmployeeDTO request) {
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