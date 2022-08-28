package adamchukpr.touragency.service;

import adamchukpr.touragency.mapper.EmployeeMapper;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.repos.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static adamchukpr.touragency.stub.EmployeeStub.getEmployeeRequest;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.PostStub.getRandomPost;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class EmployeeServiceTest {
    public static final Long ID = 1L;
    private EmployeeService service;

    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;

    @BeforeEach
    void setup() {
        service = new EmployeeService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var employee = getRandomEmployee();
        when(repository.findById(any())).thenReturn(Optional.of(employee));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), employee.getId()),
                () -> assertEquals(result.getFullName(), employee.getFullName()),
                () -> assertEquals(result.getPhone(), employee.getPhone()),
                () -> assertEquals(result.getPost(), employee.getPost()),
                () -> assertEquals(result.getTicketReservations().size(), employee.getTicketReservations().size()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(Employee.class);
        var employee = getRandomEmployee();
        when(mapper.fromRequest(any())).thenReturn(employee);
        when(repository.save(any())).thenReturn(getRandomEmployee());

        var result = service.create(getEmployeeRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(employee, captor.getValue());
        assertEquals(employee.getFullName(), result.getFullName());
        assertEquals(employee.getPost().getId(), result.getPost().getId());
    }

    @Test
    public void testSuccessfulGetAll() {
        var employee1 = getRandomEmployee();
        var employee2 = Employee.builder()
                                .id(2L)
                                .fullName("Kris")
                                .post(getRandomPost())
                                .phone("+380745632547")
                                .startDate(LocalDate.of(2021, 2, 8))
                                .endDate(LocalDate.of(2022, 2, 8))
                                .agencyServiceOrder(new ArrayList<>())
                                .ticketReservations(new ArrayList<>())
                                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(employee1.getId(), result1.getId());
            assertEquals(employee1.getFullName(), result1.getFullName());
            assertEquals(employee1.getPost(), result1.getPost());

            var result2 = resultResponse.get(1);

            assertEquals(employee2.getId(), result2.getId());
            assertEquals(employee2.getFullName(), result2.getFullName());
            assertEquals(employee2.getPost().getId(), result2.getPost().getId());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<Employee> expected = new ArrayList<>();
        Page<Employee> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<Employee> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(Employee.class);
        var employee = getRandomEmployee();


        when(repository.findById(any())).thenReturn(Optional.of(employee));
        when(repository.save(any())).thenReturn(getRandomEmployee());

        var result = service.update(ID, getEmployeeRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(employee, captor.getValue());
        assertEquals(employee.getFullName(), result.getFullName());

        assertAll(() -> {
            assertEquals(employee.getId(), result.getId());
            assertEquals(employee.getFullName(), result.getFullName());
            assertEquals(employee.getPost().getId(), result.getPost().getId());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getEmployeeRequest()));

        assertTrue(e.getMessage().contains("No Employee with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }

    @Test
    void testSuccessfulGetAllByPost() {
        Post post1 = getRandomPost();
        Post post2 = Post.builder().id(2L).positionName("Manager").build();

        Employee employee1 = getRandomEmployee();
        Employee employee2 = Employee.builder()
                                     .id(2L)
                                     .fullName("Kris")
                                     .post(getRandomPost())
                                     .phone("+380745632547")
                                     .startDate(LocalDate.of(2021, 2, 8))
                                     .endDate(LocalDate.of(2022, 2, 8))
                                     .agencyServiceOrder(new ArrayList<>())
                                     .ticketReservations(new ArrayList<>())
                                     .build();


        when(repository.findAllEmployeesByPostId(post1.getId(), null)).thenReturn(List.of(employee1, employee2));
        when(repository.findAllEmployeesByPostId(post2.getId(), null)).thenReturn(List.of());

        var result = service.getByPositionId(1L, 10, 1, "false");
        var result2 = service.getByPositionId(2L, 10, 1, "false");
        var result3 = service.getByPositionId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }
}