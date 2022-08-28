package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long>, PagingAndSortingRepository<Tourist, Long> {
}
