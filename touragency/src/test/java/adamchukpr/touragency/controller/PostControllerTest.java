package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.PostDTO;
import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.repos.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static adamchukpr.touragency.stub.EmployeeStub.getRandomEmployee;
import static adamchukpr.touragency.stub.PostStub.getRandomPost;
import static adamchukpr.touragency.stub.PostStub.getPostRequest;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void successfullyGetAll() throws Exception {
        var post1 = getRandomPost();
        var post2 = Post.builder()
                        .id(1L)
                        .positionName("position")
                        .description("description")
                        .salary(30000)
                        .build();
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));
        mockMvc.perform(getRequest("/posts"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.content().string(containsString(post1.getPositionName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(post1.getDescription())));
    }

    @Test
    void successfullyGetById() throws Exception {
        var expectedProduct = getRandomPost();
        when(postRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
        mockMvc.perform(getRequest("/posts/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getPositionName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())));
    }

    @Test
    void successfullyCreate() throws Exception {
        var request = getPostRequest();
        var expected = getRandomPost();
        when(postRepository.findById(any())).thenReturn(Optional.of(getRandomPost()));
        when(postRepository.save(any())).thenReturn(expected);
        mockMvc.perform(postRequest("/posts", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getPositionName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())));
    }

    @Test
    void successfullyUpdateById() throws Exception {
        var request = getPostRequest();
        request.setPositionName("PositionName1");
        var expected = getRandomPost();
        when(postRepository.findById(any())).thenReturn(Optional.of(expected));
        when(postRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(putRequest("/posts/1", request))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getPositionName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(expected.getDescription())));

    }

    @Test
    void successfullyDeleteById() throws Exception {
        mockMvc.perform(deleteRequest("/posts/1"))
               .andExpect(MockMvcResultMatchers.status().isOk());
        verify(postRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void successfullyGetPostForEmployee() throws Exception {
        var employee = getRandomEmployee();
        var post = getRandomPost();
        employee.setPost(post);
        when(postRepository.getPostByEmployeeId(any())).thenReturn(List.of(post));
        mockMvc.perform(getRequest("/posts/employee/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string(containsString(post.getPositionName())))
               .andExpect(MockMvcResultMatchers.content().string(containsString(employee.getPost().getDescription())));
    }

    private MockHttpServletRequestBuilder getRequest(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder putRequest(String url, PostDTO request) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder postRequest(String url, PostDTO request) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request));
    }

    private MockHttpServletRequestBuilder deleteRequest(String url) {
        return delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}