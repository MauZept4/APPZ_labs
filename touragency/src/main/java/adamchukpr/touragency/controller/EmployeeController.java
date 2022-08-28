package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.EmployeeDTO;
import adamchukpr.touragency.entity.Employee;
import adamchukpr.touragency.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@Api(value = "Employee controller")
public class EmployeeController {
    private final EmployeeService service;

    @ApiOperation(value = "Get all employees", notes = "This method for getting all employees")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<Employee> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get employee by id", notes = "This method for getting employee by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public Employee get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create employee", notes = "This method for creating employee")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public Employee create(@RequestBody EmployeeDTO employee) {
        return service.create(employee);
    }

    @ApiOperation(value = "Update employee", notes = "This method for editing employee by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        return service.update(id, employee);
    }

    @ApiOperation(value = "Delete employee", notes = "This method for deleting employee by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Get all by position id", notes = "This method for getting all employees by position id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/position/{positionId}")
    public List<Employee> getAllByPositionId(@PathVariable Long positionId,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getByPositionId(positionId, size, page, pageable);
    }
}
