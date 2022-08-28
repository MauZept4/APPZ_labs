package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.AgencyService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AgencyServiceRepository extends JpaRepository<AgencyService, Long>, PagingAndSortingRepository<AgencyService, Long> {
}
