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
@Table(name = "additional_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalService {
    
    @Id
    @Column(name = "service_id")
    private String serviceId;
    
    @NotBlank(message = "부가서비스명은 필수입니다")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "월정액은 필수입니다")
    @Positive(message = "월정액은 양수여야 합니다")
    @Column(name = "monthly_fee", nullable = false)
    private Integer monthlyFee;
    
    @Column(name = "description")
    private String description;
}
