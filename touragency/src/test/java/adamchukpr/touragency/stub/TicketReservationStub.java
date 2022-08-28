package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.TicketReservationDTO;
import adamchukpr.touragency.entity.TicketReservation;


import java.time.LocalDate;

import static adamchukpr.touragency.stub.TicketStub.getRandomTicket;
import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;

public final class TicketReservationStub {

    private static final Long ID = 1L;
    private static final Long TOTAL_PRICE = 4032L;
    private static final LocalDate START_DATE = LocalDate.of(2021, 2, 18);
    private static final LocalDate FINISH_DATE = LocalDate.of(2022, 4, 19);

    public static TicketReservation getRandomTicketReservation() {
        return TicketReservation.builder()
                              .id(ID)
                              .tourist(getRandomTourist())
                              .employee(getRandomEmployee())
                              .ticket(getRandomTicket())
                              .totalPrice(TOTAL_PRICE)
                              .startDate(START_DATE)
                              .finishDate(FINISH_DATE)
                              .build();
    }

    public static TicketReservationDTO getTicketReservationRequest() {
        var dto = new TicketReservationDTO();
        dto.setTouristId(getRandomTourist().getId());
        dto.setEmployeeId(getRandomEmployee().getId());
        dto.setTicketId(getRandomTicket().getId());
        dto.setStartDate(START_DATE);
        dto.setFinishDate(FINISH_DATE);

        return dto;
    }
}
