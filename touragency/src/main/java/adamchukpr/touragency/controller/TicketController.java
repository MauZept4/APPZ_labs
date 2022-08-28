package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TicketDTO;
import adamchukpr.touragency.entity.Ticket;
import adamchukpr.touragency.service.TicketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
@Api(value = "Ticket controller")
public class TicketController {
    private final TicketService service;

    @ApiOperation(value = "Get all tickets", notes = "This method for getting all tickets")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<Ticket> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                             @RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get ticket by id", notes = "This method for getting ticket by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create ticket", notes = "This method for creating ticket")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public Ticket create(@RequestBody TicketDTO ticket) {
        return service.create(ticket);
    }

    @ApiOperation(value = "Update ticket", notes = "This method for editing ticket by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public Ticket update(@PathVariable Long id, @RequestBody TicketDTO ticket) {
        return service.update(id, ticket);
    }

    @ApiOperation(value = "Delete ticket", notes = "This method for deleting ticket by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Get all available ticket", notes = "This method for getting all ticket that are available")
    @ApiResponse(code = 200, message = "Successful getting")
    @GetMapping("/available")
    public List<Ticket> getAllAvailable(@RequestParam(required = false, defaultValue = "10") Integer size,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllAvailable(size, page, pageable);
    }

    @ApiOperation(value = "Get all not available ticket", notes = "This method for getting all ticket that are not available")
    @ApiResponse(code = 200, message = "Successful getting")
    @GetMapping("/not-available")
    public List<Ticket> getAllNotAvailable(@RequestParam(required = false, defaultValue = "10") Integer size,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllNotAvailable(size, page, pageable);
    }

    @ApiOperation(value = "Get all tickets by ticket type id", notes = "This method for getting all ticket by ticket type id")
    @ApiResponse(code = 200, message = "Successful getting")
    @GetMapping("/ticket-type/{id}")
    public List<Ticket> getAllByTicketTypeId(@PathVariable Long id,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllByTicketTypeId(id, size, page, pageable);
    }
}
