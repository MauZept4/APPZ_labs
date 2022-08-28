package adamchukpr.touragency.entity;

import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.enums.TypeByComfort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "ticket_type")
public class TicketType {
    @Id
    private Long id;
    private String typeName;
    private double price;
    private TypeByPrice typeByPrice;
    private TypeByComfort typeByComfort;
    private String description;
    @OneToMany(mappedBy = "ticketType")
    @JsonIgnore
    @ToString.Exclude
    private List<Ticket> tickets;
}
