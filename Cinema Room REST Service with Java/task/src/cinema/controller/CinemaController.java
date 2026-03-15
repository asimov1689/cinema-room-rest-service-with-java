package cinema.controller;

import cinema.dto.ErrorResponseDto;
import cinema.dto.PurchaseRequestDto;
import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import cinema.service.CinemaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/purchase")
    public SeatDto purchaseTicket(@RequestBody PurchaseRequestDto request) {
        return cinemaService.purchaseTicket(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
