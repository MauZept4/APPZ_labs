package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.TicketReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketReservationRepository extends JpaRepository<TicketReservation, Long>, PagingAndSortingRepository<TicketReservation, Long> {
    @Query(value = "SELECT * FROM ticket_reservation WHERE ticket_id = ?1", nativeQuery = true)
    List<TicketReservation> findAllReservationsByTicketId(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM ticket_reservation WHERE tourist_id = ?1", nativeQuery = true)
    List<TicketReservation> findAllReservationsByTouristId(Long id, Pageable pageable);
}
