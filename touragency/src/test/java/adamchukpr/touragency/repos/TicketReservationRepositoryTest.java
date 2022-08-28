package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.entity.TicketType;
import adamchukpr.touragency.entity.Tourist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;

@DataJpaTest
class TicketReservationRepositoryTest {
    @Autowired
    private TicketReservationRepository repository;

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        touristRepository.deleteAll();
    }

    @Test
    void testSuccessfulGetAllByTourist() {
        Tourist tourist1 = Tourist.builder()
                               .id(1L)
                               .fullName("Anna")
                               .build();

        Tourist tourist2 = Tourist.builder()
                               .id(2L)
                               .fullName("Alex")
                               .build();

        touristRepository.save(tourist1);
        touristRepository.save(tourist2);

        TicketReservation ticketReservation1 = TicketReservation.builder().id(1L).tourist(tourist1).build();
        TicketReservation ticketReservation2 = TicketReservation.builder().id(2L).tourist(tourist1).build();
        TicketReservation ticketReservation3 = TicketReservation.builder().id(3L).tourist(tourist2).build();

        repository.save(ticketReservation1);
        repository.save(ticketReservation2);
        repository.save(ticketReservation3);

        List<TicketReservation> expected1 = List.of(ticketReservation1, ticketReservation2);
        List<TicketReservation> expected2 = List.of(ticketReservation3);

        List<TicketReservation> actual1 = repository.findAllReservationsByTouristId(1L, null);
        List<TicketReservation> actual2 = repository.findAllReservationsByTouristId(2L, null);

        Assertions.assertThat(actual1.size()).isEqualTo(expected1.size());
        Assertions.assertThat(actual2.size()).isEqualTo(expected2.size());
        Assertions.assertThat(actual2.get(0).getId()).isEqualTo(expected2.get(0).getId());
    }

    @Test
    void testSuccessfulGetAllByTicket() {
        TicketType ticketType = getRandomTicketType();
        ticketTypeRepository.save(ticketType);

        Ticket ticket1 = Ticket.builder()
                         .id(1L)
                         .ticketNumber("A33")
                         .ticketType(ticketType)
                         .build();

        Ticket ticket2 = Ticket.builder()
                         .id(2L)
                         .ticketNumber("A34")
                         .ticketType(ticketType)
                         .build();

        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        TicketReservation ticketReservation1 = TicketReservation.builder().id(1L).ticket(ticket1).build();
        TicketReservation ticketReservation2 = TicketReservation.builder().id(2L).ticket(ticket1).build();
        TicketReservation ticketReservation3 = TicketReservation.builder().id(3L).ticket(ticket2).build();

        repository.save(ticketReservation1);
        repository.save(ticketReservation2);
        repository.save(ticketReservation3);

        List<TicketReservation> expected1 = List.of(ticketReservation1, ticketReservation2);
        List<TicketReservation> expected2 = List.of(ticketReservation3);

        List<TicketReservation> actual1 = repository.findAllReservationsByTicketId(1L, null);
        List<TicketReservation> actual2 = repository.findAllReservationsByTicketId(2L, null);

        Assertions.assertThat(actual1.size()).isEqualTo(expected1.size());
        Assertions.assertThat(actual2.size()).isEqualTo(expected2.size());
        Assertions.assertThat(actual2.get(0).getId()).isEqualTo(expected2.get(0).getId());
    }
}