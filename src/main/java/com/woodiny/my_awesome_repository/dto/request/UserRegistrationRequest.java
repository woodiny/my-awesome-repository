package com.woodiny.my_awesome_repository.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    
    @NotBlank(message = "이름은 필수입니다")
    private String name;
    
    @NotBlank(message = "연락처는 필수입니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "연락처는 010-XXXX-XXXX 형식이어야 합니다")
    private String contactNumber;
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    private String email;
}
