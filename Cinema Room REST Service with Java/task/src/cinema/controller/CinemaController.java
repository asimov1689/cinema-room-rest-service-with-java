package cinema.controller;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.ReturnRequestDto;
import cinema.dto.SeatsResponseDto;
import cinema.dto.StatsResponseDto;
import cinema.dto.TicketResponseDto;
import cinema.service.CinemaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public SeatsResponseDto getSeats() {
        return cinemaService.getSeats();
    }

    @PostMapping("/purchase")
    public TicketResponseDto purchaseTicket(@RequestBody PurchaseRequestDto request) {
        return cinemaService.purchaseTicket(request);
    }

    @PostMapping("/return")
    public Map<String, ?> returnTicket(@RequestBody ReturnRequestDto request) {
        return cinemaService.returnTicket(request);
    }

    @GetMapping("/stats")
    public StatsResponseDto getStats(@RequestParam(name = "password", required = false) String password) {
        return cinemaService.getStats(password);
    }
}