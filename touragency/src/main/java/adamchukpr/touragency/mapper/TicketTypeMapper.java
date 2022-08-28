package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.TicketTypeDTO;
import adamchukpr.touragency.entity.TicketType;
import adamchukpr.touragency.repos.TicketTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TicketTypeMapper {
    private final TicketTypeRepository repo;

    public TicketType fromRequest(TicketTypeDTO request) {
        return TicketType.builder()
                       .id((long) (repo.findAll().stream()
                                       .mapToInt(el -> Math.toIntExact(el.getId()))
                                       .max()
                                       .orElse(0) + 1))
                       .typeName(request.getTypeName())
                       .price(request.getPrice())
                       .description(request.getDescription())
                       .typeByComfort(request.getTypeByComfort())
                       .typeByPrice(request.getTypeByPrice())
                       .tickets(new ArrayList<>())
                       .build();
    }

    public void copyDtoToEntity(TicketTypeDTO dto, TicketType entity) {
        entity.setTypeName(dto.getTypeName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setTypeName(dto.getTypeName());
        entity.setTypeByPrice(dto.getTypeByPrice());
    }
}
