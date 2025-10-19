package com.woodiny.my_awesome_repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @NotBlank(message = "이름은 필수입니다")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "연락처는 필수입니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "연락처는 010-XXXX-XXXX 형식이어야 합니다")
    @Column(name = "contact_number", nullable = false, unique = true)
    private String contactNumber;
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    
    @Column(name = "registration_date", nullable = false)
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();
    
    public enum UserStatus {
        ACTIVE, INACTIVE
    }
}
