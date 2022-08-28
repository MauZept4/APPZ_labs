package adamchukpr.touragency.service;

import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.enums.TypeByComfort;
import adamchukpr.touragency.mapper.TicketMapper;
import adamchukpr.touragency.repos.TicketRepository;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.entity.TicketType;
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

import static adamchukpr.touragency.stub.TicketStub.getRandomTicket;
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;
import static adamchukpr.touragency.stub.TicketStub.getTicketRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TicketServiceTest {
    public static final Long ID = 1L;
    private TicketService service;

    @Mock
    private TicketRepository repository;
    @Mock
    private TicketMapper mapper;

    @BeforeEach
    void setup() {
        service = new TicketService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var ticket = getRandomTicket();
        when(repository.findById(any())).thenReturn(Optional.of(ticket));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), ticket.getId()),
                () -> assertEquals(result.getTicketNumber(), ticket.getTicketNumber()),
                () -> assertEquals(result.getTicketType(), ticket.getTicketType()),
                () -> assertEquals(result.getTicketReservations().size(), ticket.getTicketReservations().size()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(Ticket.class);
        var ticket = getRandomTicket();
        when(mapper.fromRequest(any())).thenReturn(ticket);
        when(repository.save(any())).thenReturn(getRandomTicket());

        var result = service.create(getTicketRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(ticket, captor.getValue());
        assertEquals(ticket.getTicketNumber(), result.getTicketNumber());
        assertEquals(ticket.getTicketType().getId(), result.getTicketType().getId());
    }

    @Test
    public void testSuccessfulGetAll() {
        var ticket1 = getRandomTicket();
        var ticket2 = Ticket.builder()
                        .id(ID)
                        .ticketNumber("22C")
                        .isAvailable(true)
                        .ticketType(getRandomTicketType())
                        .TicketReservations(new ArrayList<>())
                        .build();

        when(repository.findAll()).thenReturn(Arrays.asList(ticket1, ticket2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(ticket1.getId(), result1.getId());
            assertEquals(ticket1.getTicketNumber(), result1.getTicketNumber());
            assertEquals(ticket1.getTicketType(), result1.getTicketType());

            var result2 = resultResponse.get(1);

            assertEquals(ticket2.getId(), result2.getId());
            assertEquals(ticket2.getTicketNumber(), result2.getTicketNumber());
            assertEquals(ticket2.getTicketType().getId(), result2.getTicketType().getId());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<Ticket> expected = new ArrayList<>();
        Page<Ticket> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<Ticket> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(Ticket.class);
        var ticket = getRandomTicket();


        when(repository.findById(any())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenReturn(getRandomTicket());

        var result = service.update(ID, getTicketRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(ticket, captor.getValue());
        assertEquals(ticket.getTicketNumber(), result.getTicketNumber());

        assertAll(() -> {
            assertEquals(ticket.getId(), result.getId());
            assertEquals(ticket.getTicketNumber(), result.getTicketNumber());
            assertEquals(ticket.getTicketReservations().size(), result.getTicketReservations().size());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getTicketRequest()));

        assertTrue(e.getMessage().contains("No Ticket with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }

    @Test
    void testSuccessfulGetAllAvailable() {
        Ticket ticket1 = getRandomTicket();
        Ticket ticket2 = Ticket.builder()
                         .id(2L)
                         .ticketNumber("45A")
                         .isAvailable(false)
                         .TicketReservations(new ArrayList<>())
                         .build();
        Ticket ticket3 = Ticket.builder()
                         .id(3L)
                         .ticketNumber("46A")
                         .isAvailable(true)
                         .TicketReservations(new ArrayList<>())
                         .build();
        Ticket ticket4 = Ticket.builder()
                         .id(4L)
                         .ticketNumber("47A")
                         .isAvailable(true)
                         .TicketReservations(new ArrayList<>())
                         .build();

        when(repository.findAllAvailable(null)).thenReturn(List.of(ticket1, ticket3, ticket4));

        var result = service.getAllAvailable(10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 3),
                () -> assertTrue(result.contains(ticket1)),
                () -> assertFalse(result.contains(ticket2)));
    }

    @Test
    void testSuccessfulGetAllNotAvailable() {
        Ticket ticket1 = getRandomTicket();
        Ticket ticket2 = Ticket.builder()
                         .id(2L)
                         .ticketNumber("45A")
                         .isAvailable(false)
                         .TicketReservations(new ArrayList<>())
                         .build();
        Ticket ticket3 = Ticket.builder()
                         .id(3L)
                         .ticketNumber("46A")
                         .isAvailable(true)
                         .TicketReservations(new ArrayList<>())
                         .build();
        Ticket ticket4 = Ticket.builder()
                         .id(4L)
                         .ticketNumber("47A")
                         .isAvailable(false)
                         .TicketReservations(new ArrayList<>())
                         .build();

        when(repository.findAllNotAvailable(null)).thenReturn(List.of(ticket2, ticket4));

        var result = service.getAllNotAvailable(10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertFalse(result.contains(ticket1)),
                () -> assertFalse(result.contains(ticket3)),
                () -> assertTrue(result.contains(ticket2)));
    }

    @Test
    void testSuccessfulGetAllByTicketType() {
        TicketType ticketType1 = getRandomTicketType();
        TicketType ticketType2 = TicketType.builder()
                                     .id(2L)
                                     .typeName("Some type")
                                     .typeByComfort(TypeByComfort.ECONOMY)
                                     .typeByPrice(TypeByPrice.REGULAR)
                                     .build();

        Ticket ticket1 = getRandomTicket();
        Ticket ticket2 = Ticket.builder()
                         .id(2L)
                         .ticketNumber("59S")
                         .isAvailable(true)
                         .ticketType(getRandomTicketType())
                         .build();


        when(repository.findAllByTicketType_id(ticketType1.getId(), null)).thenReturn(List.of(ticket1, ticket2));
        when(repository.findAllByTicketType_id(ticketType2.getId(), null)).thenReturn(List.of());

        var result = service.getAllByTicketTypeId(1L, 10, 1, "false");
        var result2 = service.getAllByTicketTypeId(2L, 10, 1, "false");
        var result3 = service.getAllByTicketTypeId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }
}