package adamchukpr.touragency.service;

import adamchukpr.touragency.mapper.AgencyServiceOrderMapper;
import adamchukpr.touragency.repos.AgencyServiceOrderRepository;
import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.entity.AgencyService;
import adamchukpr.touragency.entity.AgencyServiceOrder;
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

import static adamchukpr.touragency.stub.AgencyServiceStub.getRandomAgencyService;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.AgencyServiceOrderStub.getAgencyServiceOrderRequest;
import static adamchukpr.touragency.stub.AgencyServiceOrderStub.getRandomAgencyServiceOrder;
import static adamchukpr.touragency.stub.PostStub.getRandomPost;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AgencyServiceOrderServiceTest {
    public static final Long ID = 1L;
    private AgencyServiceOrderService service;

    @Mock
    private AgencyServiceOrderRepository repository;
    @Mock
    private AgencyServiceOrderMapper mapper;

    @BeforeEach
    void setup() {
        service = new AgencyServiceOrderService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var agencyServiceOrder = getRandomAgencyServiceOrder();
        when(repository.findById(any())).thenReturn(Optional.of(agencyServiceOrder));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), agencyServiceOrder.getId()),
                () -> assertEquals(result.getEmployee(), agencyServiceOrder.getEmployee()),
                () -> assertEquals(result.getTourist(), agencyServiceOrder.getTourist()),
                () -> assertEquals(result.getDate(), agencyServiceOrder.getDate()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(AgencyServiceOrder.class);
        var agencyServiceOrder = getRandomAgencyServiceOrder();
        when(mapper.fromRequest(any())).thenReturn(agencyServiceOrder);
        when(repository.save(any())).thenReturn(getRandomAgencyServiceOrder());

        var result = service.create(getAgencyServiceOrderRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(agencyServiceOrder, captor.getValue());
        assertEquals(agencyServiceOrder.getTourist().getId(), result.getTourist().getId());
        assertEquals(agencyServiceOrder.getEmployee().getId(), result.getEmployee().getId());
    }

    @Test
    public void testSuccessfulGetAll() {
        var agencyServiceOrder1 = getRandomAgencyServiceOrder();
        var agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                  .id(ID)
                                                  .tourist(getRandomTourist())
                                                  .employee(getRandomEmployee())
                                                  .agencyService(getRandomAgencyService())
                                                  .date(LocalDate.now())
                                                  .build();

        when(repository.findAll()).thenReturn(Arrays.asList(agencyServiceOrder1, agencyServiceOrder2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(agencyServiceOrder1.getId(), result1.getId());
            assertEquals(agencyServiceOrder1.getTourist(), result1.getTourist());
            assertEquals(agencyServiceOrder1.getEmployee(), result1.getEmployee());

            var result2 = resultResponse.get(1);

            assertEquals(agencyServiceOrder2.getId(), result2.getId());
            assertEquals(agencyServiceOrder2.getTourist(), result2.getTourist());
            assertEquals(agencyServiceOrder2.getEmployee().getId(), result2.getEmployee().getId());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<AgencyServiceOrder> expected = new ArrayList<>();
        Page<AgencyServiceOrder> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<AgencyServiceOrder> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(AgencyServiceOrder.class);
        var agencyServiceOrder = getRandomAgencyServiceOrder();


        when(repository.findById(any())).thenReturn(Optional.of(agencyServiceOrder));
        when(repository.save(any())).thenReturn(getRandomAgencyServiceOrder());

        var result = service.update(ID, getAgencyServiceOrderRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(agencyServiceOrder, captor.getValue());
        assertEquals(agencyServiceOrder.getTourist().getId(), result.getTourist().getId());

        assertAll(() -> {
            assertEquals(agencyServiceOrder.getId(), result.getId());
            assertEquals(agencyServiceOrder.getEmployee().getId(), result.getEmployee().getId());
            assertEquals(agencyServiceOrder.getEmployee()
                                          .getId(), result.getEmployee().getId());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getAgencyServiceOrderRequest()));

        assertTrue(e.getMessage().contains("No AgencyServiceOrder with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }

    @Test
    void testSuccessfulGetAllByTourist() {
        Tourist tourist1 = getRandomTourist();

        AgencyServiceOrder agencyServiceOrder1 = getRandomAgencyServiceOrder();
        AgencyServiceOrder agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                                .id(2L)
                                                                .tourist(getRandomTourist())
                                                                .employee(getRandomEmployee())
                                                                .date(LocalDate.now())
                                                                .agencyService(getRandomAgencyService())
                                                                .build();


        when(repository.findAllByTourist_Id(tourist1.getId(), null)).thenReturn(List.of(agencyServiceOrder1, agencyServiceOrder2));

        var result = service.getAllByTouristId(1L, 10, 1, "false");
        var result2 = service.getAllByTouristId(2L, 10, 1, "false");
        var result3 = service.getAllByTouristId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }

    @Test
    void testSuccessfulGetAllByEmployee() {
        Employee employee1 = getRandomEmployee();
        Employee employee2 = Employee.builder()
                                     .id(2L)
                                     .fullName("Olga")
                                     .post(getRandomPost())
                                     .ticketReservations(new ArrayList<>())
                                     .build();

        AgencyServiceOrder agencyServiceOrder1 = getRandomAgencyServiceOrder();
        AgencyServiceOrder agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                                .id(2L)
                                                                .tourist(getRandomTourist())
                                                                .employee(getRandomEmployee())
                                                                .date(LocalDate.now())
                                                                .agencyService(getRandomAgencyService())
                                                                .build();


        when(repository.findAllByEmployee_Id(employee1.getId(), null)).thenReturn(List.of(agencyServiceOrder1, agencyServiceOrder2));
        when(repository.findAllByEmployee_Id(employee2.getId(), null)).thenReturn(List.of());

        var result = service.getAllByEmployeeId(1L, 10, 1, "false");
        var result2 = service.getAllByEmployeeId(2L, 10, 1, "false");
        var result3 = service.getAllByEmployeeId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }

    @Test
    void testSuccessfulGetAllByService() {
        AgencyService service1 = getRandomAgencyService();
        AgencyService service2 = AgencyService.builder()
                                            .id(2L)
                                            .serviceName("Service")
                                            .price(4000L)
                                            .build();

        AgencyServiceOrder agencyServiceOrder1 = getRandomAgencyServiceOrder();
        AgencyServiceOrder agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                                .id(2L)
                                                                .tourist(getRandomTourist())
                                                                .employee(getRandomEmployee())
                                                                .date(LocalDate.now())
                                                                .agencyService(getRandomAgencyService())
                                                                .build();


        when(repository.findAllByAgencyService_Id(service1.getId(), null)).thenReturn(List.of(agencyServiceOrder1, agencyServiceOrder2));
        when(repository.findAllByAgencyService_Id(service2.getId(), null)).thenReturn(List.of());

        var result = service.getAllByService(1L, 10, 1, "false");
        var result2 = service.getAllByService(2L, 10, 1, "false");
        var result3 = service.getAllByService(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }

    @Test
    void testSuccessfulGetAllByDate() {
        LocalDate date1 = LocalDate.of(2020, 11, 3);
        LocalDate date2 = LocalDate.of(2021, 6, 17);
        LocalDate date3 = LocalDate.now();

        AgencyServiceOrder agencyServiceOrder1 = getRandomAgencyServiceOrder();
        AgencyServiceOrder agencyServiceOrder2 = AgencyServiceOrder.builder()
                                                                .id(2L)
                                                                .tourist(getRandomTourist())
                                                                .employee(getRandomEmployee())
                                                                .date(LocalDate.now())
                                                                .agencyService(getRandomAgencyService())
                                                                .build();


        when(repository.findAllByDate(date1, null)).thenReturn(List.of(agencyServiceOrder1, agencyServiceOrder2));
        when(repository.findAllByDate(date2, null)).thenReturn(List.of());

        var result = service.getAllByDate(date1, 10, 1, "false");
        var result2 = service.getAllByDate(date2, 10, 1, "false");
        var result3 = service.getAllByDate(date3, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }
}