package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.TicketReservationDTO;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.repos.TicketReservationRepository;
import adamchukpr.touragency.repos.TouristRepository;
import adamchukpr.touragency.repos.EmployeeRepository;
import adamchukpr.touragency.repos.TicketRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketReservationMapper {
    private final TicketRepository ticketRepo;
    private final TouristRepository touristRepo;
    private final EmployeeRepository employeeRepo;
    private final TicketReservationRepository repo;

    public TicketReservation fromRequest(TicketReservationDTO request) {
        var ticket = ticketRepo.findById(request.getTicketId()).orElse(null);
        var tourist = touristRepo.findById(request.getTouristId()).orElse(null);
        var employee = employeeRepo.findById(request.getEmployeeId()).orElse(null);

        return TicketReservation.builder()
                              .id((long) (repo.findAll().stream()
                                              .mapToInt(el -> Math.toIntExact(el.getId()))
                                              .max()
                                              .orElse(0) + 1))
                              .ticket(ticket)
                              .tourist(tourist)
                              .employee(employee)
                              .totalPrice(request.getTotalPrice())
                              .startDate(request.getStartDate())
                              .finishDate(request.getFinishDate())
                              .build();
    }

    public void copyDtoToEntity(TicketReservationDTO dto, TicketReservation entity) {
        var ticket = ticketRepo.findById(dto.getTicketId()).orElse(null);
        var tourist = touristRepo.findById(dto.getTouristId()).orElse(null);
        var employee = employeeRepo.findById(dto.getEmployeeId()).orElse(null);

        entity.setTicket(ticket);
        entity.setTourist(tourist);
        entity.setEmployee(employee);
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setStartDate(dto.getStartDate());
        entity.setFinishDate(dto.getFinishDate());
    }
}
