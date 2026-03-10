package cinema.service;

import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CinemaServiceTest {

    private final CinemaService cinemaService = new CinemaService();

    @Test
    void shouldReturnAllSeatsWithCorrectDimensions() {
        // Arrange

        // Act
        SeatsResponseDto response = cinemaService.getSeats();

        // Assert
        assertEquals(9, response.getRows());
        assertEquals(9, response.getColumns());

        List<SeatDto> seats = response.getSeats();
        assertEquals(81, seats.size());

        assertEquals(1, seats.get(0).getRow());
        assertEquals(1, seats.get(0).getColumn());

        assertEquals(9, seats.get(seats.size() - 1).getRow());
        assertEquals(9, seats.get(seats.size() - 1).getColumn());
    }

    @Test
    void shouldReturnSeatsInRowMajorOrder() {
        // Arrange

        // Act
        SeatsResponseDto response = cinemaService.getSeats();
        List<SeatDto> seats = response.getSeats();

        // Assert
        assertEquals(1, seats.get(0).getRow());
        assertEquals(1, seats.get(0).getColumn());

        assertEquals(1, seats.get(1).getRow());
        assertEquals(2, seats.get(1).getColumn());

        assertEquals(1, seats.get(8).getRow());
        assertEquals(9, seats.get(8).getColumn());

        assertEquals(2, seats.get(9).getRow());
        assertEquals(1, seats.get(9).getColumn());
    }
}