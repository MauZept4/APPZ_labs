package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.AgencyServiceOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgencyServiceOrderRepository extends JpaRepository<AgencyServiceOrder, Long>, PagingAndSortingRepository<AgencyServiceOrder, Long> {
    List<AgencyServiceOrder> findAllByTourist_Id(Long id, Pageable pageable);

    List<AgencyServiceOrder> findAllByEmployee_Id(Long id, Pageable pageable);

    List<AgencyServiceOrder> findAllByDate(LocalDate date, Pageable pageable);

    List<AgencyServiceOrder> findAllByAgencyService_Id(Long id, Pageable pageable);
}
