package cinema.service;

import cinema.dto.SeatDto;
import cinema.dto.SeatsResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaService {

    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLUMNS = 9;

    public SeatsResponseDto getSeats() {
        List<SeatDto> seats = new ArrayList<>();

        for (int row = 1; row <= TOTAL_ROWS; row++) {
            for (int column = 1; column <= TOTAL_COLUMNS; column++) {
                seats.add(new SeatDto(row, column));
            }
        }

        return new SeatsResponseDto(TOTAL_ROWS, TOTAL_COLUMNS, seats);
    }
}