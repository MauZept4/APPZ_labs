package adamchukpr.touragency.entity;

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
@Entity(name = "agency_service")
public class AgencyService {
    @Id
    private Long id;
    private String serviceName;
    private String description;
    private Long price;
    @OneToMany(mappedBy = "agencyService")
    @JsonIgnore
    @ToString.Exclude
    private List<AgencyServiceOrder> agencyServiceOrders;
}
