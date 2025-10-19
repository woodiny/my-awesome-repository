package com.woodiny.my_awesome_repository.dto.response;

import com.woodiny.my_awesome_repository.entity.AdditionalService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalServiceResponse {
    
    private String serviceId;
    private String name;
    private Integer monthlyFee;
    private String description;
    
    public static AdditionalServiceResponse from(AdditionalService additionalService) {
        return AdditionalServiceResponse.builder()
                .serviceId(additionalService.getServiceId())
                .name(additionalService.getName())
                .monthlyFee(additionalService.getMonthlyFee())
                .description(additionalService.getDescription())
                .build();
    }
}
