package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.TicketTypeDTO;
import adamchukpr.touragency.entity.TicketType;
import adamchukpr.touragency.mapper.TicketTypeMapper;
import adamchukpr.touragency.repos.TicketTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketTypeService {
    private final TicketTypeRepository repo;
    private final TicketTypeMapper mapper;

    public TicketType getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<TicketType> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public TicketType create(TicketTypeDTO request) {
        var ticketType = mapper.fromRequest(request);
        return repo.save(ticketType);
    }

    public TicketType update(@PathVariable Long id, @RequestBody TicketTypeDTO ticketType) {
        TicketType entity = repo.findById(id)
                              .orElseThrow(() -> new IllegalArgumentException("No TicketType with this ID: " + id));
        mapper.copyDtoToEntity(ticketType, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
