package com.woodiny.my_awesome_repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeRequest {
    
    @NotBlank(message = "새 요금제 ID는 필수입니다")
    private String newPlanId;
    
    @NotNull(message = "적용 시작일은 필수입니다")
    private LocalDate effectiveFrom;
}
