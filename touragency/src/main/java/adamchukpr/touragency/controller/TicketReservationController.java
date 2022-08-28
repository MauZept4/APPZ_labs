package adamchukpr.touragency.controller;

import adamchukpr.touragency.dto.TicketReservationDTO;
import adamchukpr.touragency.entity.TicketReservation;
import adamchukpr.touragency.service.TicketReservationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-reservations")
@Api(value = "Ticket reservation controller")
public class TicketReservationController {
    private final TicketReservationService service;

    @ApiOperation(value = "Get all ticket reservations", notes = "This method for getting all ticket reservations")
    @ApiResponse(code = 200, message = "Successful get all")
    @GetMapping()
    public List<TicketReservation> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "false") String pageable
    ) {
        return service.getAll(size, page, pageable);
    }

    @ApiOperation(value = "Get ticket reservation by id", notes = "This method for getting ticket reservation by id")
    @ApiResponse(code = 200, message = "Successful get by id")
    @GetMapping("/{id}")
    public TicketReservation get(@PathVariable Long id) {
        return service.getById(id);
    }

    @ApiOperation(value = "Create ticket reservation", notes = "This method for creating ticket reservation")
    @ApiResponse(code = 200, message = "Successful create")
    @PostMapping
    public TicketReservation create(@RequestBody TicketReservationDTO ticketReservation) {
        return service.create(ticketReservation);
    }

    @ApiOperation(value = "Update ticket reservation", notes = "This method for editing ticket reservation by id")
    @ApiResponse(code = 200, message = "Successful update")
    @PutMapping("/{id}")
    public TicketReservation update(@PathVariable Long id, @RequestBody TicketReservationDTO ticketReservation) {
        return service.update(id, ticketReservation);
    }

    @ApiOperation(value = "Delete ticket reservation", notes = "This method for deleting ticket reservation by id")
    @ApiResponse(code = 200, message = "Successful delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Get all ticket reservations by ticket id", notes = "This method for getting all ticket reservations by ticket id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/by-ticket-id/{id}")
    public List<TicketReservation> getAllByTicketId(@PathVariable Long id,
                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                                @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllReservationsByTicketId(id, size, page, pageable);
    }

    @ApiOperation(value = "Get all ticket reservations by tourist id", notes = "This method for getting all ticket reservations by tourist id")
    @ApiResponse(code = 200, message = "Successful get")
    @GetMapping("/by-tourist-id/{id}")
    public List<TicketReservation> getAllByTouristId(@PathVariable Long id,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(required = false, defaultValue = "false") String pageable) {
        return service.getAllReservationsByTouristId(id, size, page, pageable);
    }
}

