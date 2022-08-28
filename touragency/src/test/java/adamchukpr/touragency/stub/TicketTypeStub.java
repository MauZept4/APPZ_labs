package adamchukpr.touragency.stub;

import adamchukpr.touragency.enums.TypeByComfort;
import adamchukpr.touragency.enums.TypeByPrice;
import adamchukpr.touragency.dto.TicketTypeDTO;
import adamchukpr.touragency.entity.TicketType;

import java.util.ArrayList;

public final class TicketTypeStub {

    private static final Long ID = 1L;
    private static final String TYPE_NAME = "Best ticket";
    private static final double PRICE = 5000;
    private static final String DESCRIPTION = "It is the best ticket for your destination";

    public static TicketType getRandomTicketType() {
        return TicketType.builder()
                       .id(ID)
                       .typeName(TYPE_NAME)
                       .price(PRICE)
                       .typeByPrice(TypeByPrice.REGULAR)
                       .typeByComfort(TypeByComfort.PREMIUM)
                       .description(DESCRIPTION)
                       .tickets(new ArrayList<>())
                       .build();
    }

    public static TicketTypeDTO getTicketTypeRequest() {
        var dto = new TicketTypeDTO();
        dto.setTypeName(TYPE_NAME);
        dto.setPrice(PRICE);
        dto.setDescription(DESCRIPTION);
        dto.setTypeByComfort(TypeByComfort.PREMIUM);
        dto.setTypeByPrice(TypeByPrice.REGULAR);
        return dto;
    }
}
