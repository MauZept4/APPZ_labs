package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long>, PagingAndSortingRepository<TicketType, Long> {
}
