package adamchukpr.touragency.service;

import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.mapper.TicketReservationMapper;
import adamchukpr.touragency.repos.TicketReservationRepository;

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

import static adamchukpr.touragency.stub.TicketStub.getRandomTicket;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.TicketReservationStub.getRandomTicketReservation;
import static adamchukpr.touragency.stub.TicketReservationStub.getTicketReservationRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TicketReservationServiceTest {
    public static final Long ID = 1L;
    private TicketReservationService service;

    @Mock
    private TicketReservationRepository repository;
    @Mock
    private TicketReservationMapper mapper;

    @BeforeEach
    void setup() {
        service = new TicketReservationService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var ticketReservation = getRandomTicketReservation();
        when(repository.findById(any())).thenReturn(Optional.of(ticketReservation));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), ticketReservation.getId()),
                () -> assertEquals(result.getTicket(), ticketReservation.getTicket()),
                () -> assertEquals(result.getTourist(), ticketReservation.getTourist()),
                () -> assertEquals(result.getFinishDate(), ticketReservation.getFinishDate()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(TicketReservation.class);
        var ticketReservation = getRandomTicketReservation();
        when(mapper.fromRequest(any())).thenReturn(ticketReservation);
        when(repository.save(any())).thenReturn(getRandomTicketReservation());

        var result = service.create(getTicketReservationRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(ticketReservation, captor.getValue());
        assertEquals(ticketReservation.getTourist().getId(), result.getTourist().getId());
        assertEquals(ticketReservation.getEmployee().getId(), result.getEmployee().getId());
    }

    @Test
    public void testSuccessfulGetAll() {
        var ticketReservation1 = getRandomTicketReservation();
        var ticketReservation2 = TicketReservation.builder()
                                              .id(ID)
                                              .tourist(getRandomTourist())
                                              .employee(getRandomEmployee())
                                              .ticket(getRandomTicket())
                                              .totalPrice(2012L)
                                              .startDate(LocalDate.now())
                                              .finishDate(null)
                                              .build();

        when(repository.findAll()).thenReturn(Arrays.asList(ticketReservation1, ticketReservation2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(ticketReservation1.getId(), result1.getId());
            assertEquals(ticketReservation1.getTourist(), result1.getTourist());
            assertEquals(ticketReservation1.getTicket(), result1.getTicket());

            var result2 = resultResponse.get(1);

            assertEquals(ticketReservation2.getId(), result2.getId());
            assertEquals(ticketReservation2.getTourist(), result2.getTourist());
            assertEquals(ticketReservation2.getTicket().getId(), result2.getTicket().getId());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<TicketReservation> expected = new ArrayList<>();
        Page<TicketReservation> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<TicketReservation> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(TicketReservation.class);
        var ticketReservation = getRandomTicketReservation();


        when(repository.findById(any())).thenReturn(Optional.of(ticketReservation));
        when(repository.save(any())).thenReturn(getRandomTicketReservation());

        var result = service.update(ID, getTicketReservationRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(ticketReservation, captor.getValue());
        assertEquals(ticketReservation.getTourist().getId(), result.getTourist().getId());

        assertAll(() -> {
            assertEquals(ticketReservation.getId(), result.getId());
            assertEquals(ticketReservation.getEmployee().getId(), result.getEmployee().getId());
            assertEquals(ticketReservation.getTicket()
                                        .getId(), result.getTicket().getId());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getTicketReservationRequest()));

        assertTrue(e.getMessage().contains("No TicketReservation with this ID"));
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

        TicketReservation ticketReservation1 = getRandomTicketReservation();
        TicketReservation ticketReservation2 = TicketReservation.builder()
                                                          .id(2L)
                                                          .tourist(getRandomTourist())
                                                          .employee(getRandomEmployee())
                                                          .ticket(getRandomTicket())
                                                          .startDate(LocalDate.now())
                                                          .finishDate(LocalDate.now().plusDays(4))
                                                          .build();


        when(repository.findAllReservationsByTouristId(tourist1.getId(), null)).thenReturn(List.of(ticketReservation1, ticketReservation2));

        var result = service.getAllReservationsByTouristId(1L, 10, 1, "false");
        var result2 = service.getAllReservationsByTouristId(2L, 10, 1, "false");
        var result3 = service.getAllReservationsByTouristId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }

    @Test
    void testSuccessfulGetAllByTicket() {
        Ticket ticket1 = getRandomTicket();

        TicketReservation ticketReservation1 = getRandomTicketReservation();
        TicketReservation ticketReservation2 = TicketReservation.builder()
                                                          .id(2L)
                                                          .tourist(getRandomTourist())
                                                          .employee(getRandomEmployee())
                                                          .ticket(getRandomTicket())
                                                          .startDate(LocalDate.now())
                                                          .finishDate(LocalDate.now().plusDays(4))
                                                          .build();


        when(repository.findAllReservationsByTicketId(ticket1.getId(), null)).thenReturn(List.of(ticketReservation1, ticketReservation2));

        var result = service.getAllReservationsByTicketId(1L, 10, 1, "false");
        var result2 = service.getAllReservationsByTicketId(2L, 10, 1, "false");
        var result3 = service.getAllReservationsByTicketId(3L, 10, 1, "false");

        assertAll(
                () -> assertEquals(result.size(), 2),
                () -> assertEquals(result2.size(), 0),
                () -> assertEquals(result3.size(), 0));
    }
}