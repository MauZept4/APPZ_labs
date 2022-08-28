package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.EmployeeDTO;
import adamchukpr.touragency.entity.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

import static adamchukpr.touragency.stub.PostStub.getRandomPost;

public final class EmployeeStub {

    private static final Long ID = 1L;
    private static final String NAME = "Angelina";
    private static final String PHONE = "+380664755214";
    private static final LocalDate START_DATE = LocalDate.of(2021, 2, 18);
    private static final LocalDate END_DATE = LocalDate.of(2022, 4, 19);


    public static Employee getRandomEmployee() {
        return Employee.builder()
                       .id(ID)
                       .fullName(NAME)
                       .phone(PHONE)
                       .post(getRandomPost())
                       .startDate(START_DATE)
                       .endDate(END_DATE)
                       .ticketReservations(new ArrayList<>())
                       .agencyServiceOrder(new ArrayList<>())
                       .build();
    }

    public static EmployeeDTO getEmployeeRequest() {
        var dto = new EmployeeDTO();
        dto.setFullName(NAME);
        dto.setPhone(PHONE);
        dto.setPostId(getRandomPost().getId());
        dto.setStartDate(START_DATE);
        dto.setEndDate(END_DATE);
        return dto;
    }
}
