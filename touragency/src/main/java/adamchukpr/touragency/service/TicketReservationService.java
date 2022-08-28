package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.TicketReservationDTO;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.mapper.TicketReservationMapper;
import adamchukpr.touragency.repos.TicketReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketReservationService {
    private final TicketReservationRepository repo;
    private final TicketReservationMapper mapper;

    public TicketReservation getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<TicketReservation> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public TicketReservation create(TicketReservationDTO request) {
        var ticketType = mapper.fromRequest(request);
        return repo.save(ticketType);
    }

    public TicketReservation update(@PathVariable Long id, @RequestBody TicketReservationDTO ticketReservationType) {
        TicketReservation entity = repo.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("No TicketReservation with this ID: " + id));
        mapper.copyDtoToEntity(ticketReservationType, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<TicketReservation> getAllReservationsByTicketId(Long id, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllReservationsByTicketId(id, null);
        }
        return repo.findAllReservationsByTicketId(id, PageRequest.of((page - 1), size));
    }

    public List<TicketReservation> getAllReservationsByTouristId(Long id, Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAllReservationsByTouristId(id, null);
        }
        return repo.findAllReservationsByTouristId(id, PageRequest.of((page - 1), size));
    }
}
