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
@Entity(name = "agency_service_order")
public class AgencyServiceOrder {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "tourist_id")
    private Tourist tourist;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "agency_service_id")
    private AgencyService agencyService;
    private LocalDate date;
}
