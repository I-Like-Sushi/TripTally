package org.example.triptally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.triptally.travel.dto.trip.TripRequestDto;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.user.User;
import org.example.triptally.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/data.sql")
class TripControllerIntegrationTest {

    private static final Long SEED_USER_ID = 100000000L;
    private static final String SEED_USERNAME = "jdoe";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired TripRepository tripRepository;
    @Autowired UserRepository userRepository;

    @WithMockUser(username = SEED_USERNAME)
    @Test
    void createTrip_persists_entity_and_returns_201_with_location() throws Exception {
        // Arrange
        TripRequestDto dto = new TripRequestDto();
        dto.setDestination("Hawaii");
        dto.setDescription("A nice trip to Hawaii");
        dto.setStartDate(LocalDate.of(2025, 10, 10));
        dto.setEndDate(LocalDate.of(2025, 10, 20));
        dto.setBudgetHomeCurrency(new BigDecimal("2000.00"));
        dto.setHomeCurrencyCode("EUR");
        dto.setLocalCurrencyCode("JPY");

        // Guard: seeded user exists
        User seed = userRepository.findById(SEED_USER_ID)
                .orElseThrow(() -> new AssertionError("Seed user not present for id " + SEED_USER_ID));
        assertThat(seed.getUsername()).isEqualTo(SEED_USERNAME);

        // Record count before create
        Long beforeCount = tripRepository.countByUserId(SEED_USER_ID);

        // Act: perform authenticated request and capture result
        MvcResult mvcResult = mvc.perform(post("/api/v1/users/{userId}/trips", SEED_USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("^/api/users/\\d+/trips/[^/]+$")))
                .andExpect(jsonPath("$.destination").value("Hawaii"))
                .andExpect(jsonPath("$.description").value("A nice trip to Hawaii"))
                .andExpect(jsonPath("$.startDate").value("2025-10-10"))
                .andExpect(jsonPath("$.endDate").value("2025-10-20"))
                .andExpect(jsonPath("$.homeCurrencyCode").value("EUR"))
                .andExpect(jsonPath("$.localCurrencyCode").value("JPY"))
                .andReturn();

        // Debug: print response body
        String body = mvcResult.getResponse().getContentAsString();
        System.out.println("Create trip response: " + body);

        // Parse tripId from response JSON
        JsonNode root = objectMapper.readTree(body);
        JsonNode tripIdNode = root.get("tripId");
        String tripId = tripIdNode != null ? tripIdNode.asText() : null;
        assertThat(tripId).isNotNull();

        // Assert: repository count increased
        Long afterCount = tripRepository.countByUserId(SEED_USER_ID);
        assertThat(afterCount).isEqualTo(beforeCount + 1);

        // Assert: find created trip by tripId and assert fields
        Optional<Trip> createdOpt = tripRepository.findById(tripId);
        Trip created = createdOpt.orElseThrow(() -> new AssertionError("Created trip not found by id " + tripId));

        assertThat(created.getDestination()).isEqualTo("Hawaii");
        assertThat(created.getDescription()).isEqualTo("A nice trip to Hawaii");
        assertThat(created.getStartDate()).isEqualTo(LocalDate.of(2025, 10, 10));
        assertThat(created.getEndDate()).isEqualTo(LocalDate.of(2025, 10, 20));
        assertThat(created.getBudgetHomeCurrency()).isEqualByComparingTo(new BigDecimal("2000.00"));
        assertThat(created.getHomeCurrencyCode()).isEqualTo("EUR");
        assertThat(created.getLocalCurrencyCode()).isEqualTo("JPY");

        // Cross-check Location header contains created trip id
        String location = mvcResult.getResponse().getHeader("Location");
        assertThat(location).contains("/users/" + SEED_USER_ID + "/trips/" + tripId);
    }
}
