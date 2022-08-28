package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, PagingAndSortingRepository<Employee, Long> {
    @Query(value = "SELECT * FROM employee where post_id = ?1", nativeQuery = true)
    List<Employee> findAllEmployeesByPostId(Long postId, Pageable pageable);
}
