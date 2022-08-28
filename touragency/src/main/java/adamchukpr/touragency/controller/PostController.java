package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.PostDTO;
import adamchukpr.touragency.entity.Post;
import adamchukpr.touragency.service.PostService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Api(value = "Post controller")
public class PostController {
    private final PostService service;

    @ApiOperation(value = "Get all posts", notes = "This method for getting all posts")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<Post> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                             @RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get post by id", notes = "This method for getting post by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public Post get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create post", notes = "This method for creating post")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public Post create(@RequestBody PostDTO post) {
        return service.create(post);
    }

    @ApiOperation(value = "Update post", notes = "This method for editing post by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody PostDTO post) {
        return service.update(id, post);
    }

    @ApiOperation(value = "Delete post", notes = "This method for deleting post by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Get post for employee", notes = "This method for getting post for employee")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping("/employee/{id}")
    public Post getForEmployee(@PathVariable Long id) {
        return service.getPostByEmployeeId(id);
    }
}
