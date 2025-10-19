package com.woodiny.my_awesome_repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_plan_id")
    private Long userPlanId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "사용자 ID는 필수입니다")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @NotNull(message = "요금제 ID는 필수입니다")
    private Plan plan;
    
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "시작일은 필수입니다")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserPlanStatus status = UserPlanStatus.ACTIVE;
    
    public enum UserPlanStatus {
        ACTIVE, CANCELLED
    }
}
