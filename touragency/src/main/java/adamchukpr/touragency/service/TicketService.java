package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.TicketDTO;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.mapper.TicketMapper;
import adamchukpr.touragency.repos.TicketRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repo;
    private final TicketMapper mapper;

    public Ticket getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Ticket> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public Ticket create(TicketDTO request) {
        var ticketType = mapper.fromRequest(request);
        return repo.save(ticketType);
    }

    public Ticket update(@PathVariable Long id, @RequestBody TicketDTO ticketType) {
        Ticket entity = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("No Ticket with this ID: " + id));
        mapper.copyDtoToEntity(ticketType, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Ticket> getAllAvailable(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllAvailable(null);
        }
        return repo.findAllAvailable(PageRequest.of((page - 1), size));
    }

    public List<Ticket> getAllNotAvailable(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllNotAvailable(null);
        }
        return repo.findAllNotAvailable(PageRequest.of((page - 1), size));
    }

    public List<Ticket> getAllByTicketTypeId(Long ticketTypeId, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllByTicketType_id(ticketTypeId, null);
        }
        return repo.findAllByTicketType_id(ticketTypeId, PageRequest.of((page - 1), size));
    }
}
