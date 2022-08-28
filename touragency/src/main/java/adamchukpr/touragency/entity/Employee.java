package adamchukpr.touragency.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "employee")
public class Employee {
    @Id
    private Long id;
    private String fullName;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @ToString.Exclude
    private List<TicketReservation> ticketReservations;
    @OneToMany(mappedBy = "tourist")
    @JsonIgnore
    @ToString.Exclude
    private List<AgencyServiceOrder> agencyServiceOrder;
}
