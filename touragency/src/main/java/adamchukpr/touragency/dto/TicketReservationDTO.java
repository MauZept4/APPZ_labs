package adamchukpr.touragency.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketReservationDTO {
    private Long touristId;
    private Long employeeId;
    private Long ticketId;
    private Long totalPrice;
    private LocalDate startDate;
    private LocalDate finishDate;
}
