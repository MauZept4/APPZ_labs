package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.TicketDTO;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.repos.TicketRepository;
import adamchukpr.touragency.repos.TicketTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository repo;

    public Ticket fromRequest(TicketDTO request) {
        var ticketType = ticketTypeRepository.findById(request.getTicketTypeId()).orElse(null);
        return Ticket.builder()
                   .id((long) (repo.findAll().stream()
                                   .mapToInt(el -> Math.toIntExact(el.getId()))
                                   .max()
                                   .orElse(0) + 1))
                   .ticketType(ticketType)
                   .ticketNumber(request.getTicketNumber())
                   .isAvailable(request.isAvailable())
                   .TicketReservations(new ArrayList<>())
                   .build();
    }

    public void copyDtoToEntity(TicketDTO dto, Ticket entity) {
        var ticketType = ticketTypeRepository.findById(dto.getTicketTypeId()).orElse(null);

        entity.setTicketType(ticketType);
        entity.setAvailable(dto.isAvailable());
        entity.setTicketNumber(dto.getTicketNumber());
    }
}
