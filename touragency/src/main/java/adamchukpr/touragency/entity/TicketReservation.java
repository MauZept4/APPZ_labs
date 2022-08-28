package adamchukpr.touragency.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "ticket_reservation")
public class TicketReservation {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Tourist tourist;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Ticket ticket;
    private Long totalPrice;
    private LocalDate startDate;
    private LocalDate finishDate;
}