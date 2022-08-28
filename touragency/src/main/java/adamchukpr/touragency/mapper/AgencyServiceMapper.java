package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.AgencyServiceDTO;
import adamchukpr.touragency.entity.AgencyService;
import adamchukpr.touragency.repos.AgencyServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class AgencyServiceMapper {
    private final AgencyServiceRepository repo;

    public AgencyService fromRequest(AgencyServiceDTO request) {
        return AgencyService.builder()
                           .id((long) (repo.findAll().stream()
                                           .mapToInt(el -> Math.toIntExact(el.getId()))
                                           .max()
                                           .orElse(0) + 1))
                           .serviceName(request.getServiceName())
                           .description(request.getDescription())
                           .price(request.getPrice())
                           .agencyServiceOrders(new ArrayList<>())
                           .build();
    }

    public void copyDtoToEntity(AgencyServiceDTO dto, AgencyService entity) {
        entity.setServiceName(dto.getServiceName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
    }
}
