package adamchukpr.touragency.service;

import adamchukpr.touragency.mapper.AgencyServiceMapper;
import adamchukpr.touragency.repos.AgencyServiceRepository;
import adamchukpr.touragency.entity.AgencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static adamchukpr.touragency.stub.AgencyServiceStub.getRandomAgencyService;
import static adamchukpr.touragency.stub.AgencyServiceStub.getAgencyServiceRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AgencyServiceServiceTest {
    public static final Long ID = 1L;
    private AgencyServiceService service;

    @Mock
    private AgencyServiceRepository repository;
    @Mock
    private AgencyServiceMapper mapper;

    @BeforeEach
    void setup() {
        service = new AgencyServiceService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var agencyService = getRandomAgencyService();
        when(repository.findById(any())).thenReturn(Optional.of(agencyService));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), agencyService.getId()),
                () -> assertEquals(result.getServiceName(), agencyService.getServiceName()),
                () -> assertEquals(result.getDescription(), agencyService.getDescription()),
                () -> assertEquals(result.getAgencyServiceOrders().size(), agencyService.getAgencyServiceOrders().size()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(AgencyService.class);
        var agencyService = getRandomAgencyService();
        when(mapper.fromRequest(any())).thenReturn(agencyService);
        when(repository.save(any())).thenReturn(getRandomAgencyService());

        var result = service.create(getAgencyServiceRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(agencyService, captor.getValue());
        assertEquals(agencyService.getServiceName(), result.getServiceName());
    }

    @Test
    public void testSuccessfulGetAll() {
        var agencyService1 = getRandomAgencyService();
        var agencyService2 = AgencyService.builder()
                                        .id(2L)
                                        .serviceName("Some service")
                                        .description("description for some service")
                                        .price(300L)
                                        .agencyServiceOrders(new ArrayList<>())
                                        .build();

        when(repository.findAll()).thenReturn(Arrays.asList(agencyService1, agencyService2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(agencyService1.getId(), result1.getId());
            assertEquals(agencyService1.getServiceName(), result1.getServiceName());
            assertEquals(agencyService1.getDescription(), result1.getDescription());

            var result2 = resultResponse.get(1);

            assertEquals(agencyService2.getId(), result2.getId());
            assertEquals(agencyService2.getServiceName(), result2.getServiceName());
            assertEquals(agencyService2.getDescription(), result2.getDescription());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<AgencyService> expected = new ArrayList<>();
        Page<AgencyService> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<AgencyService> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(AgencyService.class);
        var agencyService = getRandomAgencyService();

        when(repository.findById(any())).thenReturn(Optional.of(agencyService));
        when(repository.save(any())).thenReturn(getRandomAgencyService());

        var result = service.update(ID, getAgencyServiceRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(agencyService, captor.getValue());
        assertEquals(agencyService.getServiceName(), result.getServiceName());

        assertAll(() -> {
            assertEquals(agencyService.getId(), result.getId());
            assertEquals(agencyService.getServiceName(), result.getServiceName());
            assertEquals(agencyService.getDescription(), result.getDescription());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getAgencyServiceRequest()));

        assertTrue(e.getMessage().contains("No AgencyService with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }

}