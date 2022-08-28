package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.AgencyServiceOrderDTO;
import adamchukpr.touragency.entity.AgencyServiceOrder;
import adamchukpr.touragency.repos.AgencyServiceRepository;
import adamchukpr.touragency.repos.TouristRepository;
import adamchukpr.touragency.repos.EmployeeRepository;
import adamchukpr.touragency.repos.AgencyServiceOrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AgencyServiceOrderMapper {
    private final TouristRepository touristRepo;
    private final AgencyServiceRepository AgencyServiceRepo;
    private final EmployeeRepository employeeRepo;
    private final AgencyServiceOrderRepository repo;


    public AgencyServiceOrder fromRequest(AgencyServiceOrderDTO request) {
        var tourist = touristRepo.findById(request.getTouristId()).orElse(null);
        var employee = employeeRepo.findById(request.getEmployeeId()).orElse(null);
        var service = AgencyServiceRepo.findById(request.getAgencyServiceId()).orElse(null);

        return AgencyServiceOrder.builder()
                                .id((long) (repo.findAll().stream()
                                                .mapToInt(el -> Math.toIntExact(el.getId()))
                                                .max()
                                                .orElse(0) + 1))
                                .date(request.getDate())
                                .agencyService(service)
                                .tourist(tourist)
                                .employee(employee)
                                .build();
    }

    public void copyDtoToEntity(AgencyServiceOrderDTO dto, AgencyServiceOrder entity) {
        var tourist = touristRepo.findById(dto.getTouristId()).orElse(null);
        var employee = employeeRepo.findById(dto.getEmployeeId()).orElse(null);
        var service = AgencyServiceRepo.findById(dto.getAgencyServiceId()).orElse(null);

        entity.setDate(dto.getDate());
        entity.setAgencyService(service);
        entity.setTourist(tourist);
        entity.setEmployee(employee);
    }
}
