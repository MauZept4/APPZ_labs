package adamchukpr.touragency.service;

import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.mapper.TouristMapper;
import adamchukpr.touragency.repos.TouristRepository;
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

import static adamchukpr.touragency.stub.TouristStub.getRandomTourist;
import static adamchukpr.touragency.stub.TouristStub.getTouristRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TouristServiceTest {
    public static final Long ID = 1L;
    private TouristService service;

    @Mock
    private TouristRepository repository;
    @Mock
    private TouristMapper mapper;

    @BeforeEach
    void setup() {
        service = new TouristService(repository, mapper);
    }

    @Test
    void testSuccessfulGetById() {
        var tourist = getRandomTourist();
        when(repository.findById(any())).thenReturn(Optional.of(tourist));

        var result = service.getById(ID);

        assertAll(
                () -> assertEquals(result.getId(), tourist.getId()),
                () -> assertEquals(result.getFullName(), tourist.getFullName()),
                () -> assertEquals(result.getAddress(), tourist.getAddress()));
    }

    @Test
    void testNotSuccessfulGetById() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        var e = assertThrows(NoSuchElementException.class, () -> service.getById(ID));
        assertEquals(e.getMessage(), "No value present");
    }

    @Test
    void testSuccessfulSave() {
        var captor = ArgumentCaptor.forClass(Tourist.class);
        var tourist = getRandomTourist();
        when(mapper.fromRequest(any())).thenReturn(tourist);
        when(repository.save(any())).thenReturn(getRandomTourist());

        var result = service.create(getTouristRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(tourist, captor.getValue());
        assertEquals(tourist.getFullName(), result.getFullName());
    }

    @Test
    public void testSuccessfulGetAll() {
        var tourist1 = getRandomTourist();
        var tourist2 = Tourist.builder()
                            .id(2L)
                            .fullName("Maria")
                            .email("maria@gmail.com")
                            .phone("+380966304756")
                            .address("Address 34")
                            .build();

        when(repository.findAll()).thenReturn(Arrays.asList(tourist1, tourist2));
        var resultResponse = service.getAll(10, 1, "false");
        assertAll(() -> {
            assertEquals(2, resultResponse.size());
            var result1 = resultResponse.get(0);

            assertEquals(tourist1.getId(), result1.getId());
            assertEquals(tourist1.getFullName(), result1.getFullName());
            assertEquals(tourist1.getAddress(), result1.getAddress());

            var result2 = resultResponse.get(1);

            assertEquals(tourist2.getId(), result2.getId());
            assertEquals(tourist2.getFullName(), result2.getFullName());
            assertEquals(tourist2.getAddress(), result2.getAddress());
        });
    }

    @Test
    public void testSuccessfulGetAllPageable() {
        List<Tourist> expected = new ArrayList<>();
        Page<Tourist> foundPage = new PageImpl<>(expected);

        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);

        List<Tourist> actual = service.getAll(10, 1, "true");

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
        verifyNoMoreInteractions(repository);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(0, pageSpecification.getPageNumber());

        assertEquals(expected, actual);
    }


    @Test
    void testSuccessfulUpdate() {
        var captor = ArgumentCaptor.forClass(Tourist.class);
        var tourist = getRandomTourist();

        when(repository.findById(any())).thenReturn(Optional.of(tourist));
        when(repository.save(any())).thenReturn(getRandomTourist());

        var result = service.update(ID, getTouristRequest());

        verify(repository, atLeast(1)).save(captor.capture());
        assertEquals(tourist, captor.getValue());
        assertEquals(tourist.getFullName(), result.getFullName());

        assertAll(() -> {
            assertEquals(tourist.getId(), result.getId());
            assertEquals(tourist.getFullName(), result.getFullName());
            assertEquals(tourist.getAddress(), result.getAddress());
        });
    }

    @Test
    void testNotSuccessfulUpdate() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        var e = assertThrows(IllegalArgumentException.class,
                () -> service.update(ID, getTouristRequest()));

        assertTrue(e.getMessage().contains("No Tourist with this ID"));
    }

    @Test
    void testSuccessfulDelete() {
        service.delete(ID);
        var captor = ArgumentCaptor.forClass(Long.class);
        verify(repository, atLeast(1)).deleteById(captor.capture());
        assertEquals(ID, captor.getValue());
    }
}