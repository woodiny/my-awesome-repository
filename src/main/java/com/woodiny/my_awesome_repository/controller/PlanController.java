package com.woodiny.my_awesome_repository.controller;

import com.woodiny.my_awesome_repository.dto.request.PlanChangeRequest;
import com.woodiny.my_awesome_repository.dto.request.PlanSubscriptionRequest;
import com.woodiny.my_awesome_repository.dto.response.PagedResponse;
import com.woodiny.my_awesome_repository.dto.response.PlanResponse;
import com.woodiny.my_awesome_repository.dto.response.UserPlanResponse;
import com.woodiny.my_awesome_repository.service.PlanService;
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
public class PlanController {
    
    private final PlanService planService;
    
    @GetMapping("/plans")
    public ResponseEntity<PagedResponse<PlanResponse>> getPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        
        log.info("요금제 목록 조회 API 호출: page={}, size={}, name={}", page, size, name);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PlanResponse> plans;
        
        if (name != null && !name.trim().isEmpty()) {
            plans = planService.getPlansByName(name, pageable);
        } else {
            plans = planService.getPlans(pageable);
        }
        
        PagedResponse<PlanResponse> response = PagedResponse.of(
                plans.getContent(),
                plans.hasNext() ? String.valueOf(page + 1) : null,
                page > 0 ? String.valueOf(page - 1) : null,
                (int) plans.getTotalElements(),
                plans.hasNext(),
                page > 0
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/plans/{planId}")
    public ResponseEntity<PlanResponse> getPlan(@PathVariable String planId) {
        log.info("요금제 조회 API 호출: planId={}", planId);
        
        PlanResponse response = planService.getPlan(planId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/users/{userId}/plan")
    public ResponseEntity<UserPlanResponse> subscribeToPlan(
            @PathVariable Long userId,
            @Valid @RequestBody PlanSubscriptionRequest request) {
        
        log.info("요금제 가입 API 호출: userId={}, request={}", userId, request);
        
        UserPlanResponse response = planService.subscribeToPlan(userId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/users/{userId}/plan")
    public ResponseEntity<UserPlanResponse> changePlan(
            @PathVariable Long userId,
            @Valid @RequestBody PlanChangeRequest request) {
        
        log.info("요금제 변경 API 호출: userId={}, request={}", userId, request);
        
        UserPlanResponse response = planService.changePlan(userId, request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{userId}/plan")
    public ResponseEntity<UserPlanResponse> getCurrentUserPlan(@PathVariable Long userId) {
        log.info("현재 요금제 조회 API 호출: userId={}", userId);
        
        UserPlanResponse response = planService.getCurrentUserPlan(userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{userId}/plan/history")
    public ResponseEntity<List<UserPlanResponse>> getUserPlanHistory(@PathVariable Long userId) {
        log.info("요금제 이력 조회 API 호출: userId={}", userId);
        
        List<UserPlanResponse> response = planService.getUserPlanHistory(userId);
        
        return ResponseEntity.ok(response);
    }
}
