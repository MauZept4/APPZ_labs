package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>, PagingAndSortingRepository<Ticket, Long> {
    @Query(value = "SELECT * FROM ticket WHERE is_available = true", nativeQuery = true)
    List<Ticket> findAllAvailable(Pageable pageable);

    @Query(value = "SELECT * FROM ticket WHERE is_available = false", nativeQuery = true)
    List<Ticket> findAllNotAvailable(Pageable pageable);

    List<Ticket> findAllByTicketType_id(Long id, Pageable pageable);
}
