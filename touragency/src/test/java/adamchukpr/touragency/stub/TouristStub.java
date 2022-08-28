package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.TouristDTO;
import adamchukpr.touragency.entity.Tourist;

import java.util.ArrayList;

public final class TouristStub {

    private static final Long ID = 1L;
    private static final String NAME = "Anna Smith";
    private static final String EMAIL = "anna@gmail.com";
    private static final String PHONE = "+380745632874";
    private static final String ADDRESS = "St. Peter 32";

    public static Tourist getRandomTourist() {
        return Tourist.builder()
                     .id(ID)
                     .fullName(NAME)
                     .email(EMAIL)
                     .phone(PHONE)
                     .address(ADDRESS)
                     .build();
    }

    public static TouristDTO getTouristRequest() {
        var dto = new TouristDTO();
        dto.setFullName(NAME);
        dto.setEmail(EMAIL);
        dto.setPhone(PHONE);
        dto.setAddress(ADDRESS);
        return dto;
    }
}
