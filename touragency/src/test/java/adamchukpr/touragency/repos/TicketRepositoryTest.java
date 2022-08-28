package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Ticket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSuccessfulGetAllAvailable() {
        Ticket ticket1 = Ticket.builder().id(1L).ticketNumber("33N").isAvailable(true).build();
        Ticket ticket2 = Ticket.builder().id(2L).ticketNumber("35N").isAvailable(true).build();
        Ticket ticket3 = Ticket.builder().id(3L).ticketNumber("39N").isAvailable(false).build();
        Ticket ticket4 = Ticket.builder().id(4L).ticketNumber("47A").isAvailable(true).build();
        Ticket ticket5 = Ticket.builder().id(5L).ticketNumber("93B").isAvailable(false).build();
        Ticket ticket6 = Ticket.builder().id(6L).ticketNumber("31M").isAvailable(true).build();

        repository.save(ticket1);
        repository.save(ticket2);
        repository.save(ticket3);
        repository.save(ticket4);
        repository.save(ticket5);
        repository.save(ticket6);

        List<Ticket> expected = List.of(ticket1, ticket2, ticket4, ticket6);
        List<Ticket> actual = repository.findAllAvailable(null);

        Assertions.assertThat(expected.size()).isEqualTo(actual.size());
        Assertions.assertThat(expected.contains(ticket1)).isEqualTo(true);
        Assertions.assertThat(expected.contains(ticket3)).isEqualTo(false);
    }

    @Test
    void testSuccessfulGetAllNotAvailable() {
        Ticket ticket1 = Ticket.builder().id(1L).ticketNumber("33N").isAvailable(true).build();
        Ticket ticket2 = Ticket.builder().id(2L).ticketNumber("35N").isAvailable(true).build();
        Ticket ticket3 = Ticket.builder().id(3L).ticketNumber("39N").isAvailable(false).build();
        Ticket ticket4 = Ticket.builder().id(4L).ticketNumber("47A").isAvailable(true).build();
        Ticket ticket5 = Ticket.builder().id(5L).ticketNumber("93B").isAvailable(false).build();
        Ticket ticket6 = Ticket.builder().id(6L).ticketNumber("31M").isAvailable(true).build();

        repository.save(ticket1);
        repository.save(ticket2);
        repository.save(ticket3);
        repository.save(ticket4);
        repository.save(ticket5);
        repository.save(ticket6);

        List<Ticket> expected = List.of(ticket3, ticket5);
        List<Ticket> actual = repository.findAllNotAvailable(null);

        Assertions.assertThat(expected.size()).isEqualTo(actual.size());
        Assertions.assertThat(expected.contains(ticket3)).isEqualTo(true);
        Assertions.assertThat(expected.contains(ticket1)).isEqualTo(false);

    }
}