package com.woodiny.my_awesome_repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_additional_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdditionalService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_add_service_id")
    private Long userAddServiceId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "사용자 ID는 필수입니다")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "부가서비스 ID는 필수입니다")
    private AdditionalService additionalService;
    
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "시작일은 필수입니다")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserAdditionalServiceStatus status = UserAdditionalServiceStatus.ACTIVE;
    
    public enum UserAdditionalServiceStatus {
        ACTIVE, CANCELLED
    }
}
