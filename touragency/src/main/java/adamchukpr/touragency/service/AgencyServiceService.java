package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.AgencyServiceDTO;
import adamchukpr.touragency.entity.AgencyService;
import adamchukpr.touragency.mapper.AgencyServiceMapper;
import adamchukpr.touragency.repos.AgencyServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgencyServiceService {
    private final AgencyServiceRepository repo;
    private final AgencyServiceMapper mapper;

    public AgencyService getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<AgencyService> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public AgencyService create(AgencyServiceDTO request) {
        var agencyService = mapper.fromRequest(request);
        return repo.save(agencyService);
    }

    public AgencyService update(@PathVariable Long id, @RequestBody AgencyServiceDTO agencyService) {
        AgencyService entity = repo.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("No AgencyService with this ID: " + id));
        mapper.copyDtoToEntity(agencyService, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
