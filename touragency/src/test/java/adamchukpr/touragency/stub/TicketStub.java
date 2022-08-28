package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.TicketDTO;
import adamchukpr.touragency.entity.Ticket;


import java.util.ArrayList;

import static adamchukpr.touragency.stub.TicketTypeStub.getRandomTicketType;

public final class TicketStub {

    private static final Long ID = 1L;
    private static final String ROOM_NUMBER = "32A";

    public static Ticket getRandomTicket() {
        return Ticket.builder()
                   .id(ID)
                   .ticketNumber(ROOM_NUMBER)
                   .isAvailable(true)
                   .ticketType(getRandomTicketType())
                   .TicketReservations(new ArrayList<>())
                   .build();
    }

    public static TicketDTO getTicketRequest() {
        var dto = new TicketDTO();
        dto.setTicketNumber(ROOM_NUMBER);
        dto.setAvailable(false);
        dto.setTicketTypeId(getRandomTicketType().getId());
        return dto;
    }
}
