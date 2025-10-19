package com.woodiny.my_awesome_repository.service;

import com.woodiny.my_awesome_repository.dto.request.PlanChangeRequest;
import com.woodiny.my_awesome_repository.dto.request.PlanSubscriptionRequest;
import com.woodiny.my_awesome_repository.dto.response.PlanResponse;
import com.woodiny.my_awesome_repository.dto.response.UserPlanResponse;
import com.woodiny.my_awesome_repository.entity.Plan;
import com.woodiny.my_awesome_repository.entity.User;
import com.woodiny.my_awesome_repository.entity.UserPlan;
import com.woodiny.my_awesome_repository.exception.BusinessException;
import com.woodiny.my_awesome_repository.exception.ErrorCode;
import com.woodiny.my_awesome_repository.repository.PlanRepository;
import com.woodiny.my_awesome_repository.repository.UserPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlanService {
    
    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final UserService userService;
    
    public Page<PlanResponse> getPlans(Pageable pageable) {
        log.info("요금제 목록 조회 요청");
        
        Page<Plan> plans = planRepository.findAll(pageable);
        return plans.map(PlanResponse::from);
    }
    
    public Page<PlanResponse> getPlansByName(String name, Pageable pageable) {
        log.info("요금제 검색 요청: name={}", name);
        
        Page<Plan> plans = planRepository.findByNameContaining(name, pageable);
        return plans.map(PlanResponse::from);
    }
    
    public PlanResponse getPlan(String planId) {
        log.info("요금제 조회 요청: planId={}", planId);
        
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_NOT_FOUND));
        
        return PlanResponse.from(plan);
    }
    
    @Transactional
    public UserPlanResponse subscribeToPlan(Long userId, PlanSubscriptionRequest request) {
        log.info("요금제 가입 요청: userId={}, planId={}", userId, request.getPlanId());
        
        // 사용자 검증
        User user = userService.findUserById(userId);
        userService.validateUserActive(user);
        
        // 요금제 존재 확인
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_NOT_FOUND));
        
        // 기존 활성 요금제 확인
        Optional<UserPlan> existingActivePlan = userPlanRepository.findActiveByUserId(userId);
        if (existingActivePlan.isPresent()) {
            throw new BusinessException(ErrorCode.PLAN_ALREADY_SUBSCRIBED);
        }
        
        UserPlan userPlan = UserPlan.builder()
                .user(user)
                .plan(plan)
                .startDate(request.getEffectiveFrom())
                .status(UserPlan.UserPlanStatus.ACTIVE)
                .build();
        
        UserPlan savedUserPlan = userPlanRepository.save(userPlan);
        log.info("요금제 가입 완료: userPlanId={}", savedUserPlan.getUserPlanId());
        
        return UserPlanResponse.from(savedUserPlan);
    }
    
    @Transactional
    public UserPlanResponse changePlan(Long userId, PlanChangeRequest request) {
        log.info("요금제 변경 요청: userId={}, newPlanId={}", userId, request.getNewPlanId());
        
        // 사용자 검증
        User user = userService.findUserById(userId);
        userService.validateUserActive(user);
        
        // 새 요금제 존재 확인
        Plan newPlan = planRepository.findById(request.getNewPlanId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_NOT_FOUND));
        
        // 현재 활성 요금제 확인
        UserPlan currentActivePlan = userPlanRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_NOT_SUBSCRIBED));
        
        // 동일한 요금제로 변경 시도
        if (currentActivePlan.getPlan().getPlanId().equals(request.getNewPlanId())) {
            throw new BusinessException(ErrorCode.PLAN_SAME_SUBSCRIPTION);
        }
        
        // 기존 요금제 종료
        currentActivePlan.setEndDate(request.getEffectiveFrom().minusDays(1));
        currentActivePlan.setStatus(UserPlan.UserPlanStatus.CANCELLED);
        userPlanRepository.save(currentActivePlan);
        
        // 새 요금제 가입
        UserPlan newUserPlan = UserPlan.builder()
                .user(user)
                .plan(newPlan)
                .startDate(request.getEffectiveFrom())
                .status(UserPlan.UserPlanStatus.ACTIVE)
                .build();
        
        UserPlan savedUserPlan = userPlanRepository.save(newUserPlan);
        log.info("요금제 변경 완료: userPlanId={}", savedUserPlan.getUserPlanId());
        
        return UserPlanResponse.from(savedUserPlan);
    }
    
    public UserPlanResponse getCurrentUserPlan(Long userId) {
        log.info("현재 요금제 조회 요청: userId={}", userId);
        
        UserPlan userPlan = userPlanRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_NOT_SUBSCRIBED));
        
        return UserPlanResponse.from(userPlan);
    }
    
    public List<UserPlanResponse> getUserPlanHistory(Long userId) {
        log.info("요금제 이력 조회 요청: userId={}", userId);
        
        List<UserPlan> userPlans = userPlanRepository.findAllByUserIdOrderByStartDateDesc(userId);
        return userPlans.stream()
                .map(UserPlanResponse::from)
                .toList();
    }
    
    public boolean hasActivePlan(Long userId) {
        return userPlanRepository.existsByUserUserIdAndStatus(userId, UserPlan.UserPlanStatus.ACTIVE);
    }
}
