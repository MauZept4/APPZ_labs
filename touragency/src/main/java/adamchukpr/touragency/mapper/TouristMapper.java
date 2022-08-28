package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.TouristDTO;
import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.repos.TouristRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TouristMapper {

    private final TouristRepository repo;

    public Tourist fromRequest(TouristDTO request) {
        return Tourist.builder()
                     .id((long) (repo.findAll().stream()
                                     .mapToInt(el -> Math.toIntExact(el.getId()))
                                     .max()
                                     .orElse(0) + 1))
                     .fullName(request.getFullName())
                     .phone(request.getPhone())
                     .address(request.getAddress())
                     .email(request.getEmail())
                     .build();
    }

    public void copyDtoToEntity(TouristDTO dto, Tourist entity) {
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
    }
}
