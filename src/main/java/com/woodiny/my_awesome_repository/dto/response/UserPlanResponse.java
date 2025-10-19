package com.woodiny.my_awesome_repository.dto.response;

import com.woodiny.my_awesome_repository.entity.UserPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPlanResponse {
    
    private Long userPlanId;
    private Long userId;
    private String planId;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserPlan.UserPlanStatus status;
    
    public static UserPlanResponse from(UserPlan userPlan) {
        return UserPlanResponse.builder()
                .userPlanId(userPlan.getUserPlanId())
                .userId(userPlan.getUser().getUserId())
                .planId(userPlan.getPlan().getPlanId())
                .startDate(userPlan.getStartDate())
                .endDate(userPlan.getEndDate())
                .status(userPlan.getStatus())
                .build();
    }
}
