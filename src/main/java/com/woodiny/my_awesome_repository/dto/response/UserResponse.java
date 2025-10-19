package com.woodiny.my_awesome_repository.dto.response;

import com.woodiny.my_awesome_repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long userId;
    private String name;
    private String contactNumber;
    private String email;
    private User.UserStatus status;
    private LocalDateTime registrationDate;
    
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .contactNumber(user.getContactNumber())
                .email(user.getEmail())
                .status(user.getStatus())
                .registrationDate(user.getRegistrationDate())
                .build();
    }
}
