package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.AgencyServiceOrderDTO;
import adamchukpr.touragency.entity.AgencyServiceOrder;
import adamchukpr.touragency.service.AgencyServiceOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agency-service-orders")
@Api(value = "Agency service order controller")
public class AgencyServiceOrderController {
    private final AgencyServiceOrderService service;

    @ApiOperation(value = "Get all agency service orders", notes = "This method for getting all agency service orders")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<AgencyServiceOrder> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get agency service order by id", notes = "This method for getting agency service order by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public AgencyServiceOrder get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create agency service order", notes = "This method for creating agency service order")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public AgencyServiceOrder create(@RequestBody AgencyServiceOrderDTO agencyServiceOrder) {
        return service.create(agencyServiceOrder);
    }

    @ApiOperation(value = "Update agency service order", notes = "This method for editing agency service order by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public AgencyServiceOrder update(@PathVariable Long id, @RequestBody AgencyServiceOrderDTO agencyServiceOrder) {
        return service.update(id, agencyServiceOrder);
    }

    @ApiOperation(value = "Delete agency service order", notes = "This method for deleting agency service order by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Get all by tourist id", notes = "This method for getting all orders by tourist id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/tourist/{touristId}")
    public List<AgencyServiceOrder> getAllByTouristId(@PathVariable Long touristId,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllByTouristId(touristId, size, page, pageable);
    }

    @ApiOperation(value = "Get all by employee id", notes = "This method for getting all orders by employee id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/employee/{employee}")
    public List<AgencyServiceOrder> getAllByEmployee(@PathVariable Long employee,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllByEmployeeId(employee, size, page, pageable);
    }

    @ApiOperation(value = "Get all by date", notes = "This method for getting all orders by date")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/date/{date}")
    public List<AgencyServiceOrder> getAllByDate(@PathVariable String date,
                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                                @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllByDate(LocalDate.parse(date), size, page, pageable);
    }

    @ApiOperation(value = "Get all by service id", notes = "This method for getting all orders by service id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/service/{serviceId}")
    public List<AgencyServiceOrder> getAllByServiceId(@PathVariable Long serviceId,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllByService(serviceId, size, page, pageable);
    }
}
