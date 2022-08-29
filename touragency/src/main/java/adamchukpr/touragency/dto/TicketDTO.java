package adamchukpr.touragency.dto;

import lombok.Data;

@Data
public class TicketDTO {
    private String ticketNumber;
    private boolean isAvailable;
    private Long ticketTypeId;
}
