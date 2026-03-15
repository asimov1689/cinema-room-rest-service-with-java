package cinema.service;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import cinema.dto.TicketResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(10, seats.get(0).getPrice());

        assertEquals(9, seats.get(seats.size() - 1).getRow());
        assertEquals(9, seats.get(seats.size() - 1).getColumn());
        assertEquals(8, seats.get(seats.size() - 1).getPrice());
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

    @Test
    void shouldPurchaseAvailableSeat() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(3, 4);

        // Act
        TicketResponseDto response = cinemaService.purchaseTicket(request);

        // Assert
        assertEquals(3, response.getTicket().getRow());
        assertEquals(4, response.getTicket().getColumn());
        assertEquals(10, response.getTicket().getPrice());
        assertEquals(80, cinemaService.getSeats().getSeats().size());
    }

    @Test
    void shouldThrowExceptionWhenSeatAlreadyPurchased() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(3, 4);
        cinemaService.purchaseTicket(request);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(request)
        );

        // Assert
        assertEquals("The ticket has been already purchased!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRowIsOutOfBounds() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(10, 1);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(request)
        );

        // Assert
        assertEquals("The number of a row or a column is out of bounds!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenColumnIsOutOfBounds() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(1, 10);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(request)
        );

        // Assert
        assertEquals("The number of a row or a column is out of bounds!", exception.getMessage());
    }
}