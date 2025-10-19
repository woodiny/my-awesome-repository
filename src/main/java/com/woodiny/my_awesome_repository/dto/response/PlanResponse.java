package com.woodiny.my_awesome_repository.dto.response;

import com.woodiny.my_awesome_repository.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    
    private String planId;
    private String name;
    private Integer monthlyFee;
    private Integer includedVoiceMinutes;
    private Integer includedSmsCount;
    private Integer includedDataGB;
    private String description;
    
    public static PlanResponse from(Plan plan) {
        return PlanResponse.builder()
                .planId(plan.getPlanId())
                .name(plan.getName())
                .monthlyFee(plan.getMonthlyFee())
                .includedVoiceMinutes(plan.getIncludedVoiceMinutes())
                .includedSmsCount(plan.getIncludedSmsCount())
                .includedDataGB(plan.getIncludedDataGB())
                .description(plan.getDescription())
                .build();
    }
}
