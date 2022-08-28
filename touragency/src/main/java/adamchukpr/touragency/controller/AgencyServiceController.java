package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.AgencyServiceDTO;
import adamchukpr.touragency.entity.AgencyService;
import adamchukpr.touragency.service.AgencyServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agency-services")
@Api(value = "Agency service controller")
public class AgencyServiceController {
    private final AgencyServiceService service;

    @ApiOperation(value = "Get all agency services", notes = "This method for getting all agency services")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<AgencyService> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get agency service by id", notes = "This method for getting agency service by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public AgencyService get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create agency service", notes = "This method for creating agency service")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public AgencyService create(@RequestBody AgencyServiceDTO agencyService) {
        return service.create(agencyService);
    }

    @ApiOperation(value = "Update agency service", notes = "This method for editing agency service by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public AgencyService update(@PathVariable Long id, @RequestBody AgencyServiceDTO agencyService) {
        return service.update(id, agencyService);
    }

    @ApiOperation(value = "Delete agency service", notes = "This method for deleting agency service by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}

