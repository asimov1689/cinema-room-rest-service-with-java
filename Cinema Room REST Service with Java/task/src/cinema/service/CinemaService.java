package cinema.service;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.ReturnRequestDto;
import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import cinema.dto.StatsResponseDto;
import cinema.dto.TicketResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CinemaService {

    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLUMNS = 9;

    // Loaded from application.properties; override via CINEMA_STATS_PASSWORD env var in production
    @Value("${cinema.stats.password:super_secret}")
    private String statsPassword = "super_secret";

    private final List<SeatDto> availableSeats = new CopyOnWriteArrayList<>();
    private final Map<UUID, SeatDto> purchasedTickets = new ConcurrentHashMap<>();
    private final Object seatLock = new Object();

    public CinemaService() {
        for (int row = 1; row <= TOTAL_ROWS; row++) {
            for (int column = 1; column <= TOTAL_COLUMNS; column++) {
                availableSeats.add(new SeatDto(row, column));
            }
        }
    }

    public SeatsResponseDto getSeats() {
        return new SeatsResponseDto(TOTAL_ROWS, TOTAL_COLUMNS, new ArrayList<>(availableSeats));
    }

    public TicketResponseDto purchaseTicket(PurchaseRequestDto request) {
        validateSeatBounds(request.getRow(), request.getColumn());

        synchronized (seatLock) {
            SeatDto seatToPurchase = availableSeats.stream()
                    .filter(seat -> seat.getRow() == request.getRow() && seat.getColumn() == request.getColumn())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("The ticket has been already purchased!"));

            availableSeats.remove(seatToPurchase);
            UUID token = UUID.randomUUID();
            purchasedTickets.put(token, seatToPurchase);
            return new TicketResponseDto(token, seatToPurchase);
        }
    }

    public Map<String, SeatDto> returnTicket(ReturnRequestDto request) {
        if (request.getToken() == null) {
            throw new IllegalArgumentException("Wrong token!");
        }

        UUID token = request.getToken();
        SeatDto seatToReturn = purchasedTickets.remove(token);
        if (seatToReturn == null) {
            throw new IllegalArgumentException("Wrong token!");
        }

        availableSeats.add(seatToReturn);
        return Map.of("ticket", seatToReturn);
    }

    public StatsResponseDto getStats(String password) {
        if (!statsPassword.equals(password)) {
            throw new IllegalStateException("The password is wrong!");
        }

        int income = purchasedTickets.values().stream()
                .mapToInt(SeatDto::getPrice)
                .sum();

        return new StatsResponseDto(income, availableSeats.size(), purchasedTickets.size());
    }

    private void validateSeatBounds(int row, int column) {
        if (row < 1 || row > TOTAL_ROWS || column < 1 || column > TOTAL_COLUMNS) {
            throw new IllegalArgumentException("The number of a row or a column is out of bounds!");
        }
    }
}