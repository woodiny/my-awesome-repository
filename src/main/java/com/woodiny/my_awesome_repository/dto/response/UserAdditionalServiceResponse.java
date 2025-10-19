package com.woodiny.my_awesome_repository.dto.response;

import com.woodiny.my_awesome_repository.entity.UserAdditionalService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdditionalServiceResponse {
    
    private Long userAddServiceId;
    private Long userId;
    private String serviceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserAdditionalService.UserAdditionalServiceStatus status;
    
    public static UserAdditionalServiceResponse from(UserAdditionalService userAdditionalService) {
        return UserAdditionalServiceResponse.builder()
                .userAddServiceId(userAdditionalService.getUserAddServiceId())
                .userId(userAdditionalService.getUser().getUserId())
                .serviceId(userAdditionalService.getAdditionalService().getServiceId())
                .startDate(userAdditionalService.getStartDate())
                .endDate(userAdditionalService.getEndDate())
                .status(userAdditionalService.getStatus())
                .build();
    }
}
