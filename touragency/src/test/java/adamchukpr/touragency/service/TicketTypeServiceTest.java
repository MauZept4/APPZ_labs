package adamchukpr.touragency.service;

import adamchukpr.touragency.entity.TicketType;
import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.enums.TypeByComfort;
import adamchukpr.touragency.mapper.TicketTypeMapper;
import adamchukpr.touragency.repos.TicketTypeRepository;
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
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static adamchukpr.touragency.stub.TicketTypeStub.getTicketTypeRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TicketTypeServiceTest {
    public static final Long ID = 1L;
    private TicketTypeService service;

    @Mock
    private TicketTypeRepository repository;
    @Mock
    private TicketTypeMapper mapper;

    @BeforeEach
    void setup() {
        service = new TicketTypeService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var tourist = getRandomTicketType();
        when(repository.findById(any())).thenReturn(Optional.of(tourist));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), tourist.getId()),
                () -> assertEquals(result.getTypeName(), tourist.getTypeName()),
                () -> assertEquals(result.getTypeByComfort(), tourist.getTypeByComfort()),
                () -> assertEquals(result.getTickets().size(), tourist.getTickets().size()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(TicketType.class);
        var tourist = getRandomTicketType();
        when(mapper.fromRequest(any())).thenReturn(tourist);
        when(repository.save(any())).thenReturn(getRandomTicketType());

        var result = service.create(getTicketTypeRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(tourist, captor.getValue());
        assertEquals(tourist.getTypeName(), result.getTypeName());
    }

    @Test
    public void testSuccessfulGetAll() {
        var tourist1 = getRandomTicketType();
        var tourist2 = TicketType.builder()
                              .id(ID)
                              .typeName("Not best")
                              .price(200)
                              .typeByPrice(TypeByPrice.REGULAR)
                              .typeByComfort(TypeByComfort.PREMIUM)
                              .description("Some description")
                              .tickets(new ArrayList<>())
                              .build();

        when(repository.findAll()).thenReturn(Arrays.asList(tourist1, tourist2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(tourist1.getId(), result1.getId());
            assertEquals(tourist1.getTypeName(), result1.getTypeName());
            assertEquals(tourist1.getTypeByComfort(), result1.getTypeByComfort());

            var result2 = resultResponse.get(1);

            assertEquals(tourist2.getId(), result2.getId());
            assertEquals(tourist2.getTypeName(), result2.getTypeName());
            assertEquals(tourist2.getTypeByComfort(), result2.getTypeByComfort());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<TicketType> expected = new ArrayList<>();
        Page<TicketType> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<TicketType> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(TicketType.class);
        var tourist = getRandomTicketType();

        when(repository.findById(any())).thenReturn(Optional.of(tourist));
        when(repository.save(any())).thenReturn(getRandomTicketType());

        var result = service.update(ID, getTicketTypeRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(tourist, captor.getValue());
        assertEquals(tourist.getTypeName(), result.getTypeName());

        assertAll(() -> {
            assertEquals(tourist.getId(), result.getId());
            assertEquals(tourist.getTypeName(), result.getTypeName());
            assertEquals(tourist.getTypeByComfort(), result.getTypeByComfort());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getTicketTypeRequest()));

        assertTrue(e.getMessage().contains("No TicketType with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }
}