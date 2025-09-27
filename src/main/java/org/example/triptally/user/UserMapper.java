package org.example.triptally.user;

import org.example.triptally.travel.mapper.TripMapper;
import org.example.triptally.user.dto.UserRequestDto;
import org.example.triptally.user.dto.UserResponseDto;
import org.example.triptally.user.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final TripMapper tripMapper;

    public UserMapper(TripMapper tripMapper) {
        this.tripMapper = tripMapper;
    }

    public UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRoles(user.getRoles());
        dto.setEnabled(user.isEnabled());
        dto.setAccountCreatedAt(user.getAccountCreatedAt());
        return dto;
    }

    public UserResponseDto restrictedView(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(user.getUsername());
        return dto;
    }

    public UserResponseDto allowedAccesView(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(user.getUsername());
        dto.setBio(user.getBio());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setTrips(user.getTrips().stream()
                .map(tripMapper::toDto)
                .toList());


        return dto;
    }

    public User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setPassword(dto.getPassword());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        return user;
    }

    public void updateEntityFromDto(UserUpdateDto dto, User user) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getBio() != null) user.setBio(dto.getBio());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());
    }

    public void changeAccountStatus(UserUpdateDto dto, User user) {
        user.setEnabled(dto.isEnabled());
    }
}
