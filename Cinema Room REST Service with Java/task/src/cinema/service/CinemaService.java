package cinema.service;

import cinema.dto.PurchaseRequestDto;
import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CinemaService {

    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLUMNS = 9;

    private final List<SeatDto> availableSeats = new CopyOnWriteArrayList<>();
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

    public SeatDto purchaseTicket(PurchaseRequestDto request) {
        if (request.getRow() < 1 || request.getRow() > TOTAL_ROWS ||
                request.getColumn() < 1 || request.getColumn() > TOTAL_COLUMNS) {
            throw new IllegalArgumentException("The number of a row or a column is out of bounds!");
        }

        synchronized (seatLock) {
            SeatDto seatToPurchase = availableSeats.stream()
                    .filter(seat -> seat.getRow() == request.getRow() && seat.getColumn() == request.getColumn())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("The ticket has been already purchased!"));
            availableSeats.remove(seatToPurchase);
            return seatToPurchase;
        }
    }
}