package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.AgencyServiceOrderDTO;
import adamchukpr.touragency.entity.AgencyServiceOrder;
import adamchukpr.touragency.mapper.AgencyServiceOrderMapper;
import adamchukpr.touragency.repos.AgencyServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AgencyServiceOrderService {
    private final AgencyServiceOrderRepository repo;
    private final AgencyServiceOrderMapper mapper;

    public AgencyServiceOrder getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<AgencyServiceOrder> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public AgencyServiceOrder create(AgencyServiceOrderDTO request) {
        var agencyServiceOrder = mapper.fromRequest(request);
        return repo.save(agencyServiceOrder);
    }

    public AgencyServiceOrder update(@PathVariable Long id, @RequestBody AgencyServiceOrderDTO agencyServiceOrder) {
        AgencyServiceOrder entity = repo.findById(id)
                                       .orElseThrow(() -> new IllegalArgumentException("No AgencyServiceOrder with this ID: " + id));
        mapper.copyDtoToEntity(agencyServiceOrder, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<AgencyServiceOrder> getAllByTouristId(Long clientId, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllByTourist_Id(clientId, null);
        }
        return repo.findAllByTourist_Id(clientId, PageRequest.of((page - 1), size));
    }

    public List<AgencyServiceOrder> getAllByEmployeeId(Long employeeId, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllByEmployee_Id(employeeId, null);
        }
        return repo.findAllByEmployee_Id(employeeId, PageRequest.of((page - 1), size));
    }

    public List<AgencyServiceOrder> getAllByDate(LocalDate date, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllByDate(date, null);
        }
        return repo.findAllByDate(date, PageRequest.of((page - 1), size));
    }

    public List<AgencyServiceOrder> getAllByService(Long serviceId, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllByAgencyService_Id(serviceId, null);
        }
        return repo.findAllByAgencyService_Id(serviceId, PageRequest.of((page - 1), size));
    }
}
