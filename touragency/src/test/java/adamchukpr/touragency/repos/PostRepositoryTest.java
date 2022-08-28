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
class PostRepositoryTest {

    @Autowired
    private PostRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void testSuccessfulGetPostForEmployee() {
        Post post1 = Post.builder().id(1L).positionName("Administrator").build();
        Post post2 = Post.builder().id(2L).positionName("Manager").build();

        repository.save(post1);
        repository.save(post2);

        System.out.println(repository.findAll());

        Employee employee1 = Employee.builder().id(1L).fullName("Anna").post(post1).build();
        Employee employee2 = Employee.builder().id(2L).fullName("Alex").post(post1).build();
        Employee employee3 = Employee.builder().id(3L).fullName("Olga").post(post2).build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        List<Post> actual1 = repository.getPostByEmployeeId(employee1.getId());
        List<Post> actual2 = repository.getPostByEmployeeId(employee2.getId());
        List<Post> actual3 = repository.getPostByEmployeeId(employee3.getId());

        Assertions.assertThat(post1.getId()).isEqualTo(actual1.get(0).getId());
        Assertions.assertThat(post1.getId()).isEqualTo(actual2.get(0).getId());
        Assertions.assertThat(post2.getId()).isEqualTo(actual3.get(0).getId());

    }
}