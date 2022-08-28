package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.TouristDTO;
import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.mapper.TouristMapper;
import adamchukpr.touragency.repos.TouristRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TouristService {
    private final TouristRepository repo;
    private final TouristMapper mapper;

    public Tourist getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Tourist> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public Tourist create(TouristDTO request) {
        var tourist = mapper.fromRequest(request);
        return repo.save(tourist);
    }

    public Tourist update(@PathVariable Long id, @RequestBody TouristDTO tourist) {
        Tourist entity = repo.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("No Tourist with this ID: " + id));
        mapper.copyDtoToEntity(tourist, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
