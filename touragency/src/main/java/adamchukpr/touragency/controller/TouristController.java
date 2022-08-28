package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TouristDTO;
import adamchukpr.touragency.entity.Tourist;
import adamchukpr.touragency.service.TouristService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tourists")
@Api(value = "Tourist controller")
public class TouristController {
    private final TouristService service;

    @ApiOperation(value = "Get all tourists", notes = "This method for getting all tourists")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<Tourist> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                               @RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get tourist by id", notes = "This method for getting tourist by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public Tourist get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create tourist", notes = "This method for creating tourist")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public Tourist create(@RequestBody TouristDTO tourist) {
        return service.create(tourist);
    }

    @ApiOperation(value = "Update tourist", notes = "This method for editing tourist by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public Tourist update(@PathVariable Long id, @RequestBody TouristDTO tourist) {
        return service.update(id, tourist);
    }

    @ApiOperation(value = "Delete tourist", notes = "This method for deleting tourist by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
