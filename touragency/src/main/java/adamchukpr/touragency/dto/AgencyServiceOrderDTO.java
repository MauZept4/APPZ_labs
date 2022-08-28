package adamchukpr.touragency.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AgencyServiceOrderDTO {
    private Long touristId;
    private Long agencyServiceId;
    private Long employeeId;
    private LocalDate date;
}
