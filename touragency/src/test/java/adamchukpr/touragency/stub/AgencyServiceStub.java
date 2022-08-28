package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.AgencyServiceDTO;
import adamchukpr.touragency.entity.AgencyService;


import java.util.ArrayList;

public final class AgencyServiceStub {

    private static final String DESCRIPTION = "Some description";
    private static final Long ID = 1L;
    private static final String SERVICE_NAME = "Service name";

    public static AgencyService getRandomAgencyService() {
        return AgencyService.builder()
                           .id(ID)
                           .serviceName(SERVICE_NAME)
                           .description(DESCRIPTION)
                           .price(300L)
                           .agencyServiceOrders(new ArrayList<>())
                           .build();
    }

    public static AgencyServiceDTO getAgencyServiceRequest() {
        var dto = new AgencyServiceDTO();
        dto.setServiceName(SERVICE_NAME);
        dto.setDescription(DESCRIPTION);
        dto.setPrice(400L);
        return dto;
    }
}
