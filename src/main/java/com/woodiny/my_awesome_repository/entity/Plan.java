package com.woodiny.my_awesome_repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    
    @Id
    @Column(name = "plan_id")
    private String planId;
    
    @NotBlank(message = "요금제명은 필수입니다")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "월정액은 필수입니다")
    @Positive(message = "월정액은 양수여야 합니다")
    @Column(name = "monthly_fee", nullable = false)
    private Integer monthlyFee;
    
    @NotNull(message = "기본 음성 제공량은 필수입니다")
    @Column(name = "included_voice_minutes", nullable = false)
    private Integer includedVoiceMinutes;
    
    @NotNull(message = "기본 문자 제공량은 필수입니다")
    @Column(name = "included_sms_count", nullable = false)
    private Integer includedSmsCount;
    
    @NotNull(message = "기본 데이터 제공량은 필수입니다")
    @Column(name = "included_data_gb", nullable = false)
    private Integer includedDataGB;
    
    @Column(name = "description")
    private String description;
}
