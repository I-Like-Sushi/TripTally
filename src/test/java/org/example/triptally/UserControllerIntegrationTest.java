package org.example.triptally;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.triptally.user.User;
import org.example.triptally.user.UserRepository;
import org.example.triptally.user.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class UserControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;

    @Test
    void createUser_persists_entity_and_returns_201_with_location() throws Exception {
        // Arrange
        UserRequestDto request = new UserRequestDto();
        request.setUsername("UsernameTest_123");
        request.setPassword("Secret_123");
        request.setFirstName("Hans");
        request.setLastName("Gerda");
        request.setEmail("Hans@example.com");
        request.setDateOfBirth(LocalDate.of(1990, 5, 20));

        // Act
        var result = mvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("^/users/\\d+$")))
                .andExpect(jsonPath("$.username").value("UsernameTest_123"))
                .andExpect(jsonPath("$.firstName").value("Hans"))
                .andExpect(jsonPath("$.lastName").value("Gerda"))
                .andExpect(jsonPath("$.email").value("Hans@example.com"))
                .andReturn();

        // Assert
        User saved = userRepository.findByUsername("UsernameTest_123")
                .orElseThrow(() -> new AssertionError("User was not persisted"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isEnabled()).isTrue();
        assertThat(saved.getRoles()).contains("ROLE_USER");
        assertThat(saved.getAccountCreatedAt()).isNotNull();
        assertThat(saved.getPassword()).isNotBlank();
        assertThat(saved.getPassword()).doesNotContain("Secret_123");

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isEqualTo("/users/" + saved.getId());
    }
}
