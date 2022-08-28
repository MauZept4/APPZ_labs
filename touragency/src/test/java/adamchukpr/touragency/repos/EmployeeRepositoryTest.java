package adamchukpr.touragency.repos;

import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.entity.Post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSuccessfulGetEmployeesByPost() {
        Post post1 = Post.builder()
                         .id(1L)
                         .positionName("Administrator")
                         .build();

        Post post2 = Post.builder()
                         .id(2L)
                         .positionName("Manager")
                         .build();

        postRepository.save(post1);
        postRepository.save(post2);

        Employee employee1 = Employee.builder().id(1L).fullName("Anna").post(post1).build();
        Employee employee2 = Employee.builder().id(2L).fullName("Olga").post(post2).build();
        Employee employee3 = Employee.builder().id(3L).fullName("Alex").post(post1).build();

        repository.save(employee1);
        repository.save(employee2);
        repository.save(employee3);

        List<Employee> expected1 = List.of(employee1, employee3);
        List<Employee> expected2 = List.of(employee2);

        List<Employee> actual1 = repository.findAllEmployeesByPostId(1L, null);
        List<Employee> actual2 = repository.findAllEmployeesByPostId(2L, null);

        Assertions.assertThat(actual1.size()).isEqualTo(expected1.size());
        Assertions.assertThat(actual2.size()).isEqualTo(expected2.size());
        Assertions.assertThat(actual2.get(0).getId()).isEqualTo(expected2.get(0).getId());
    }

}