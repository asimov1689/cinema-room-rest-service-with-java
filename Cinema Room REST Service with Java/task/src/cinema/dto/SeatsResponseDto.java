package cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SeatsResponseDto {

    @JsonProperty("rows")
    private int rows;

    @JsonProperty("columns")
    private int columns;

    @JsonProperty("seats")
    private List<SeatDto> seats;

    public SeatsResponseDto() {
    }

    public SeatsResponseDto(int rows, int columns, List<SeatDto> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<SeatDto> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDto> seats) {
        this.seats = seats;
    }
}