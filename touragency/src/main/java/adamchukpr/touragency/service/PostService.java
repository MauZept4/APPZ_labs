package adamchukpr.touragency.service;

import adamchukpr.touragency.dto.PostDTO;
import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.mapper.PostMapper;
import adamchukpr.touragency.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repo;
    private final PostMapper mapper;

    public Post getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Post> getAll(Integer size, Integer page, String pageable) {
        if (Objects.equals(pageable, "false")) {
            return repo.findAll();
        }
        return repo.findAll(PageRequest.of((page - 1), size)).getContent();
    }

    public Post create(PostDTO request) {
        var post = mapper.fromRequest(request);
        return repo.save(post);
    }

    public Post update(@PathVariable Long id, @RequestBody PostDTO post) {
        Post entity = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("No Post with this ID: " + id));
        mapper.copyDtoToEntity(post, entity);
        return repo.save(entity);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Post getPostByEmployeeId(Long employeeId) {
        return repo.getPostByEmployeeId(employeeId).get(0);
    }
}
