package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.EmployeeDTO;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.mapper.EmployeeMapper;
import adamchukpr.touragency.repos.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;
    private final EmployeeMapper mapper;

    public Employee getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Employee> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public Employee create(EmployeeDTO request) {
        var employee = mapper.fromRequest(request);
        return repo.save(employee);
    }

    public Employee update(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        Employee entity = repo.findById(id)
                              .orElseThrow(() -> new IllegalArgumentException("No Employee with this ID: " + id));
        mapper.copyDtoToEntity(employee, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Employee> getByPositionId(Long positionId, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllEmployeesByPostId(positionId, null);
        }
        return repo.findAllEmployeesByPostId(positionId, PageRequest.of((page - 1), size));
    }
}
