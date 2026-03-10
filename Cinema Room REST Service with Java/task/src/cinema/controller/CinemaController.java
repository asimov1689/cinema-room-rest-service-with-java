package cinema.controller;

import cinema.dto.SeatsResponseDto;
import cinema.service.CinemaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
