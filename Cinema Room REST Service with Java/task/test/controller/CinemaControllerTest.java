package cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeatsEndpointResponse() throws Exception {
        // Arrange

        // Act + Assert
        mockMvc.perform(get("/seats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rows").value(9))
                .andExpect(jsonPath("$.columns").value(9))
                .andExpect(jsonPath("$.seats.length()").value(81))
                .andExpect(jsonPath("$.seats[0].row").value(1))
                .andExpect(jsonPath("$.seats[0].column").value(1))
                .andExpect(jsonPath("$.seats[80].row").value(9))
                .andExpect(jsonPath("$.seats[80].column").value(9));
    }
}