package adamchukpr.touragency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "ticket")
public class Ticket {
    @Id
    private Long id;
    private String ticketNumber;
    private boolean isAvailable;
    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;
    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    @ToString.Exclude
    private List<TicketReservation> TicketReservations;
}
