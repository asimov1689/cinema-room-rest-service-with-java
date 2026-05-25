package cinema.service;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.ReturnRequestDto;
import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import cinema.dto.StatsResponseDto;
import cinema.dto.TicketResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CinemaServiceTest {

    private final CinemaService cinemaService = new CinemaService("super_secret");

    @Test
    void shouldReturnAllSeatsInitially() {
        // Act
        SeatsResponseDto response = cinemaService.getSeats();

        // Assert
        assertEquals(9, response.getRows());
        assertEquals(9, response.getColumns());
        assertEquals(81, response.getSeats().size());
    }

    @Test
    void shouldReturnSeatsInRowMajorOrder() {
        // Act
        List<SeatDto> seats = cinemaService.getSeats().getSeats();

        // Assert
        assertEquals(1, seats.get(0).getRow());
        assertEquals(1, seats.get(0).getColumn());
        assertEquals(1, seats.get(1).getRow());
        assertEquals(2, seats.get(1).getColumn());
        assertEquals(2, seats.get(9).getRow());
        assertEquals(1, seats.get(9).getColumn());
    }

    @Test
    void shouldPurchaseTicketSuccessfully() {
        // Act
        TicketResponseDto response = cinemaService.purchaseTicket(new PurchaseRequestDto(3, 4));

        // Assert
        assertNotNull(response.getToken());
        SeatDto seat = response.getTicket();
        assertEquals(3, seat.getRow());
        assertEquals(4, seat.getColumn());
        assertEquals(10, seat.getPrice());
        assertEquals(80, cinemaService.getSeats().getSeats().size());
    }

    @Test
    void shouldThrowExceptionWhenTicketAlreadyPurchased() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(3, 4);
        cinemaService.purchaseTicket(request);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(request)
        );
        assertEquals("The ticket has been already purchased!", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRowIsOutOfBounds() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(new PurchaseRequestDto(10, 1))
        );
        assertEquals("The number of a row or a column is out of bounds!", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenColumnIsOutOfBounds() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(new PurchaseRequestDto(1, 10))
        );
        assertEquals("The number of a row or a column is out of bounds!", ex.getMessage());
    }

    @Test
    void shouldReturnTicketSuccessfully() {
        // Arrange
        TicketResponseDto purchased = cinemaService.purchaseTicket(new PurchaseRequestDto(3, 4));

        // Act
        cinemaService.returnTicket(new ReturnRequestDto(purchased.getToken()));

        // Assert
        assertEquals(81, cinemaService.getSeats().getSeats().size());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsWrong() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.returnTicket(new ReturnRequestDto(java.util.UUID.randomUUID()))
        );
        assertEquals("Wrong token!", ex.getMessage());
    }

    @Test
    void shouldReturnInitialStatsWithCorrectPassword() {
        // Act
        StatsResponseDto stats = cinemaService.getStats("super_secret");

        // Assert
        assertEquals(0, stats.getIncome());
        assertEquals(81, stats.getAvailable());
        assertEquals(0, stats.getPurchased());
    }

    @Test
    void shouldReturnUpdatedStatsAfterPurchases() {
        // Arrange
        cinemaService.purchaseTicket(new PurchaseRequestDto(1, 1));

        // Act
        StatsResponseDto stats = cinemaService.getStats("super_secret");

        // Assert
        assertEquals(10, stats.getIncome());
        assertEquals(80, stats.getAvailable());
        assertEquals(1, stats.getPurchased());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {
        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> cinemaService.getStats("wrong_password")
        );
        assertEquals("The password is wrong!", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsMissing() {
        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> cinemaService.getStats(null)
        );
        assertEquals("The password is wrong!", ex.getMessage());
    }
}
