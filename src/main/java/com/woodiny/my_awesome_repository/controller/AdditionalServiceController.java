package com.woodiny.my_awesome_repository.controller;

import com.woodiny.my_awesome_repository.dto.request.AdditionalServiceSubscriptionRequest;
import com.woodiny.my_awesome_repository.dto.response.AdditionalServiceResponse;
import com.woodiny.my_awesome_repository.dto.response.PagedResponse;
import com.woodiny.my_awesome_repository.dto.response.UserAdditionalServiceResponse;
import com.woodiny.my_awesome_repository.service.AdditionalServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AdditionalServiceController {
    
    private final AdditionalServiceService additionalServiceService;
    
    @GetMapping("/additional-services")
    public ResponseEntity<PagedResponse<AdditionalServiceResponse>> getAdditionalServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        
        log.info("부가서비스 목록 조회 API 호출: page={}, size={}, name={}", page, size, name);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AdditionalServiceResponse> services;
        
        if (name != null && !name.trim().isEmpty()) {
            services = additionalServiceService.getAdditionalServicesByName(name, pageable);
        } else {
            services = additionalServiceService.getAdditionalServices(pageable);
        }
        
        PagedResponse<AdditionalServiceResponse> response = PagedResponse.of(
                services.getContent(),
                services.hasNext() ? String.valueOf(page + 1) : null,
                page > 0 ? String.valueOf(page - 1) : null,
                (int) services.getTotalElements(),
                services.hasNext(),
                page > 0
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/additional-services/{serviceId}")
    public ResponseEntity<AdditionalServiceResponse> getAdditionalService(@PathVariable String serviceId) {
        log.info("부가서비스 조회 API 호출: serviceId={}", serviceId);
        
        AdditionalServiceResponse response = additionalServiceService.getAdditionalService(serviceId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/users/{userId}/additional-services")
    public ResponseEntity<UserAdditionalServiceResponse> subscribeToAdditionalService(
            @PathVariable Long userId,
            @Valid @RequestBody AdditionalServiceSubscriptionRequest request) {
        
        log.info("부가서비스 가입 API 호출: userId={}, request={}", userId, request);
        
        UserAdditionalServiceResponse response = additionalServiceService.subscribeToAdditionalService(userId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("/users/{userId}/additional-services/{userAddServiceId}")
    public ResponseEntity<Void> unsubscribeFromAdditionalService(
            @PathVariable Long userId,
            @PathVariable Long userAddServiceId) {
        
        log.info("부가서비스 해지 API 호출: userId={}, userAddServiceId={}", userId, userAddServiceId);
        
        additionalServiceService.unsubscribeFromAdditionalService(userId, userAddServiceId);
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/users/{userId}/additional-services")
    public ResponseEntity<List<UserAdditionalServiceResponse>> getUserAdditionalServices(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        
        log.info("사용자 부가서비스 목록 조회 API 호출: userId={}, activeOnly={}", userId, activeOnly);
        
        List<UserAdditionalServiceResponse> response;
        
        if (activeOnly) {
            response = additionalServiceService.getActiveUserAdditionalServices(userId);
        } else {
            response = additionalServiceService.getUserAdditionalServices(userId);
        }
        
        return ResponseEntity.ok(response);
    }
}
