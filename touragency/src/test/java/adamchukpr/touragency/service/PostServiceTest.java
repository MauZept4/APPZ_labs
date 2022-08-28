package adamchukpr.touragency.service;

import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.mapper.PostMapper;
import adamchukpr.touragency.repos.PostRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static adamchukpr.touragency.stub.PostStub.getRandomPost;
import static adamchukpr.touragency.stub.PostStub.getPostRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PostServiceTest {
    public static final Long ID = 1L;
    private PostService service;

    @Mock
    private PostRepository repository;
    @Mock
    private PostMapper mapper;

    @BeforeEach
    void setup() {
        service = new PostService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var post = getRandomPost();
        when(repository.findById(any())).thenReturn(Optional.of(post));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), post.getId()),
                () -> assertEquals(result.getPositionName(), post.getPositionName()),
                () -> assertEquals(result.getDescription(), post.getDescription()),
                () -> assertEquals(result.getEmployees().size(), post.getEmployees().size()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(Post.class);
        var post = getRandomPost();
        when(mapper.fromRequest(any())).thenReturn(post);
        when(repository.save(any())).thenReturn(getRandomPost());

        var result = service.create(getPostRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(post, captor.getValue());
        assertEquals(post.getPositionName(), result.getPositionName());
    }

    @Test
    public void testSuccessfulGetAll() {
        var post1 = getRandomPost();
        var post2 = Post.builder()
                        .id(2L)
                        .positionName("Manager")
                        .description("Managing processes")
                        .build();

        when(repository.findAll()).thenReturn(Arrays.asList(post1, post2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(post1.getId(), result1.getId());
            assertEquals(post1.getPositionName(), result1.getPositionName());
            assertEquals(post1.getDescription(), result1.getDescription());

            var result2 = resultResponse.get(1);

            assertEquals(post2.getId(), result2.getId());
            assertEquals(post2.getPositionName(), result2.getPositionName());
            assertEquals(post2.getDescription(), result2.getDescription());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<Post> expected = new ArrayList<>();
        Page<Post> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<Post> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(Post.class);
        var post = getRandomPost();

        when(repository.findById(any())).thenReturn(Optional.of(post));
        when(repository.save(any())).thenReturn(getRandomPost());

        var result = service.update(ID, getPostRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(post, captor.getValue());
        assertEquals(post.getPositionName(), result.getPositionName());

        assertAll(() -> {
            assertEquals(post.getId(), result.getId());
            assertEquals(post.getPositionName(), result.getPositionName());
            assertEquals(post.getDescription(), result.getDescription());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getPostRequest()));

        assertTrue(e.getMessage().contains("No Post with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }
}