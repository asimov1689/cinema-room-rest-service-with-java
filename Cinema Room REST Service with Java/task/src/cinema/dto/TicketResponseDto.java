package cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class TicketResponseDto {

    private UUID token;
    private SeatDto ticket;

    public TicketResponseDto() {
    }

    public TicketResponseDto(UUID token, SeatDto ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public SeatDto getTicket() {
        return ticket;
    }

    public void setTicket(SeatDto ticket) {
        this.ticket = ticket;
    }
}
