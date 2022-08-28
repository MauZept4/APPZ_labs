package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    @Query(value = "SELECT * FROM post where (SELECT post_id FROM employee where employee.id = ?1) = id", nativeQuery = true)
    List<Post> getPostByEmployeeId(Long employeeId);
}
