package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.EmployeeDTO;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.repos.EmployeeRepository;
import adamchukpr.touragency.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class EmployeeMapper {

    private final PostRepository postRepository;
    private final EmployeeRepository repo;

    public Employee fromRequest(EmployeeDTO request) {
        var post = postRepository.findById(request.getPostId()).orElse(null);
        return Employee.builder()
                       .id((long) (repo.findAll().stream()
                                       .mapToInt(el -> Math.toIntExact(el.getId()))
                                       .max()
                                       .orElse(0) + 1))
                       .fullName(request.getFullName())
                       .phone(request.getPhone())
                       .post(post)
                       .startDate(request.getStartDate())
                       .endDate(request.getEndDate())
                       .ticketReservations(new ArrayList<>())
                       .agencyServiceOrder(new ArrayList<>())
                       .build();
    }

    public void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
        var post = postRepository.findById(dto.getPostId()).orElse(null);

        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setPost(post);
    }
}
