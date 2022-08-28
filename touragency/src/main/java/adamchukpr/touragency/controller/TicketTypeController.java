package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TicketTypeDTO;
import adamchukpr.touragency.entity.TicketType;
import adamchukpr.touragency.service.TicketTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-types")
@Api(value = "Ticket type controller")
public class TicketTypeController {
    private final TicketTypeService service;

    @ApiOperation(value = "Get all ticket types", notes = "This method for getting all ticket types")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<TicketType> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get ticket type by id", notes = "This method for getting ticket type by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public TicketType get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create ticket type", notes = "This method for creating ticket type")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public TicketType create(@RequestBody TicketTypeDTO ticketType) {
        return service.create(ticketType);
    }

    @ApiOperation(value = "Update ticket type", notes = "This method for editing ticket type by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public TicketType update(@PathVariable Long id, @RequestBody TicketTypeDTO ticketType) {
        return service.update(id, ticketType);
    }

    @ApiOperation(value = "Delete ticket type", notes = "This method for deleting ticket type by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
