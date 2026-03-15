package cinema.service;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.ReturnRequestDto;
import cinema.dto.SeatsResponseDto;
import cinema.dto.StatsResponseDto;
import cinema.dto.TicketResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CinemaServiceTest {

    private CinemaService cinemaService;

    @BeforeEach
    void setUp() {
        cinemaService = new CinemaService();
    }

    @Test
    void shouldReturnAllSeatsInitially() {
        // Arrange

        // Act
        SeatsResponseDto response = cinemaService.getSeats();

        // Assert
        assertEquals(9, response.getRows());
        assertEquals(9, response.getColumns());
        assertEquals(81, response.getSeats().size());
    }

    @Test
    void shouldPurchaseTicketSuccessfully() {
        // Arrange
        PurchaseRequestDto request = new PurchaseRequestDto(3, 4);

        // Act
        TicketResponseDto response = cinemaService.purchaseTicket(request);

        // Assert
        assertNotNull(response.getToken());
        assertEquals(3, response.getTicket().getRow());
        assertEquals(4, response.getTicket().getColumn());
        assertEquals(10, response.getTicket().getPrice());
    }

    @Test
    void shouldThrowExceptionWhenTicketAlreadyPurchased() {
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
        PurchaseRequestDto request = new PurchaseRequestDto(10, 4);

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
        PurchaseRequestDto request = new PurchaseRequestDto(3, 10);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.purchaseTicket(request)
        );

        // Assert
        assertEquals("The number of a row or a column is out of bounds!", exception.getMessage());
    }

    @Test
    void shouldReturnTicketSuccessfully() {
        // Arrange
        PurchaseRequestDto purchaseRequest = new PurchaseRequestDto(3, 4);
        TicketResponseDto purchasedTicket = cinemaService.purchaseTicket(purchaseRequest);
        ReturnRequestDto returnRequest = new ReturnRequestDto(purchasedTicket.getToken());

        // Act
        Map<String, ?> response = cinemaService.returnTicket(returnRequest);

        // Assert
        assertNotNull(response.get("ticket"));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsWrong() {
        // Arrange
        ReturnRequestDto request = new ReturnRequestDto(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cinemaService.returnTicket(request)
        );

        // Assert
        assertEquals("Wrong token!", exception.getMessage());
    }

    @Test
    void shouldReturnInitialStatsWithCorrectPassword() {
        // Arrange

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
        cinemaService.purchaseTicket(new PurchaseRequestDto(5, 1));

        // Act
        StatsResponseDto stats = cinemaService.getStats("super_secret");

        // Assert
        assertEquals(18, stats.getIncome());
        assertEquals(79, stats.getAvailable());
        assertEquals(2, stats.getPurchased());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {
        // Arrange

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> cinemaService.getStats("wrong_password")
        );

        // Assert
        assertEquals("The password is wrong!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsMissing() {
        // Arrange

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> cinemaService.getStats(null)
        );

        // Assert
        assertEquals("The password is wrong!", exception.getMessage());
    }
}