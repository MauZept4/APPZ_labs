package adamchukpr.touragency.dto;

import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.enums.TypeByComfort;

import lombok.Data;

@Data
public class TicketTypeDTO {
    private String typeName;
    private double price;
    private TypeByPrice TypeByPrice;
    private TypeByComfort TypeByComfort;
    private String description;
}
