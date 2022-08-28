package adamchukpr.touragency.mapper;

import adamchukpr.touragency.dto.PostDTO;
import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostRepository repo;

    public Post fromRequest(PostDTO request) {
        return Post.builder()
                   .id((long) (repo.findAll().stream()
                                   .mapToInt(el -> Math.toIntExact(el.getId()))
                                   .max()
                                   .orElse(0) + 1))
                   .positionName(request.getPositionName())
                   .description(request.getDescription())
                   .salary(request.getSalary())
                   .employees(new ArrayList<>())
                   .build();
    }

    public void copyDtoToEntity(PostDTO dto, Post entity) {
        entity.setPositionName(dto.getPositionName());
        entity.setDescription(dto.getDescription());
        entity.setSalary(dto.getSalary());
    }
}
