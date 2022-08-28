package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.AgencyServiceOrderDTO;
import adamchukpr.touragency.entity.AgencyServiceOrder;


import java.time.LocalDate;
import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.AgencyServiceStub.getRandomAgencyService;

public final class AgencyServiceOrderStub {

    private static final Long ID = 1L;

    public static AgencyServiceOrder getRandomAgencyServiceOrder() {
        return AgencyServiceOrder.builder()
                                .id(ID)
                                .tourist(getRandomTourist())
                                .employee(getRandomEmployee())
                                .agencyService(getRandomAgencyService())
                                .date(LocalDate.now())
                                .build();

    }

    public static AgencyServiceOrderDTO getAgencyServiceOrderRequest() {
        var dto = new AgencyServiceOrderDTO();
        dto.setTouristId(getRandomTourist().getId());
        dto.setEmployeeId(getRandomEmployee().getId());
        dto.setAgencyServiceId(getRandomAgencyService().getId());
        dto.setDate(LocalDate.now());

        return dto;
    }
}
