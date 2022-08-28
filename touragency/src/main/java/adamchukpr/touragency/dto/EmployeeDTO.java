package adamchukpr.touragency.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private String fullName;
    private String phone;
    private Long postId;
    private LocalDate startDate;
    private LocalDate endDate;
}
