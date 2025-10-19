package com.woodiny.my_awesome_repository.service;

import com.woodiny.my_awesome_repository.dto.request.AdditionalServiceSubscriptionRequest;
import com.woodiny.my_awesome_repository.dto.response.AdditionalServiceResponse;
import com.woodiny.my_awesome_repository.dto.response.UserAdditionalServiceResponse;
import com.woodiny.my_awesome_repository.entity.AdditionalService;
import com.woodiny.my_awesome_repository.entity.User;
import com.woodiny.my_awesome_repository.entity.UserAdditionalService;
import com.woodiny.my_awesome_repository.exception.BusinessException;
import com.woodiny.my_awesome_repository.exception.ErrorCode;
import com.woodiny.my_awesome_repository.repository.AdditionalServiceRepository;
import com.woodiny.my_awesome_repository.repository.UserAdditionalServiceRepository;
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
public class AdditionalServiceService {
    
    private final AdditionalServiceRepository additionalServiceRepository;
    private final UserAdditionalServiceRepository userAdditionalServiceRepository;
    private final UserService userService;
    private final PlanService planService;
    
    public Page<AdditionalServiceResponse> getAdditionalServices(Pageable pageable) {
        log.info("부가서비스 목록 조회 요청");
        
        Page<AdditionalService> services = additionalServiceRepository.findAll(pageable);
        return services.map(AdditionalServiceResponse::from);
    }
    
    public Page<AdditionalServiceResponse> getAdditionalServicesByName(String name, Pageable pageable) {
        log.info("부가서비스 검색 요청: name={}", name);
        
        Page<AdditionalService> services = additionalServiceRepository.findByNameContaining(name, pageable);
        return services.map(AdditionalServiceResponse::from);
    }
    
    public AdditionalServiceResponse getAdditionalService(String serviceId) {
        log.info("부가서비스 조회 요청: serviceId={}", serviceId);
        
        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDITIONAL_SERVICE_NOT_FOUND));
        
        return AdditionalServiceResponse.from(service);
    }
    
    @Transactional
    public UserAdditionalServiceResponse subscribeToAdditionalService(Long userId, AdditionalServiceSubscriptionRequest request) {
        log.info("부가서비스 가입 요청: userId={}, serviceId={}", userId, request.getServiceId());
        
        // 사용자 검증
        User user = userService.findUserById(userId);
        userService.validateUserActive(user);
        
        // 부가서비스 존재 확인
        AdditionalService service = additionalServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDITIONAL_SERVICE_NOT_FOUND));
        
        // 활성 요금제 확인 (부가서비스 가입을 위해서는 활성 요금제가 필요)
        if (!planService.hasActivePlan(userId)) {
            throw new BusinessException(ErrorCode.ADDITIONAL_SERVICE_REQUIRES_PLAN);
        }
        
        // 이미 가입된 부가서비스 확인
        Optional<UserAdditionalService> existingService = userAdditionalServiceRepository
                .findActiveByUserIdAndServiceId(userId, request.getServiceId());
        if (existingService.isPresent()) {
            throw new BusinessException(ErrorCode.ADDITIONAL_SERVICE_ALREADY_SUBSCRIBED);
        }
        
        UserAdditionalService userAdditionalService = UserAdditionalService.builder()
                .user(user)
                .additionalService(service)
                .startDate(request.getEffectiveFrom())
                .status(UserAdditionalService.UserAdditionalServiceStatus.ACTIVE)
                .build();
        
        UserAdditionalService savedService = userAdditionalServiceRepository.save(userAdditionalService);
        log.info("부가서비스 가입 완료: userAddServiceId={}", savedService.getUserAddServiceId());
        
        return UserAdditionalServiceResponse.from(savedService);
    }
    
    @Transactional
    public void unsubscribeFromAdditionalService(Long userId, Long userAddServiceId) {
        log.info("부가서비스 해지 요청: userId={}, userAddServiceId={}", userId, userAddServiceId);
        
        // 사용자 검증
        User user = userService.findUserById(userId);
        userService.validateUserActive(user);
        
        // 부가서비스 가입 내역 확인
        UserAdditionalService userAdditionalService = userAdditionalServiceRepository.findById(userAddServiceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDITIONAL_SERVICE_NOT_FOUND));
        
        // 사용자 소유 확인
        if (!userAdditionalService.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ADDITIONAL_SERVICE_NOT_FOUND);
        }
        
        // 이미 해지된 서비스 확인
        if (userAdditionalService.getStatus() == UserAdditionalService.UserAdditionalServiceStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.ADDITIONAL_SERVICE_NOT_SUBSCRIBED);
        }
        
        // 부가서비스 해지
        userAdditionalService.setEndDate(LocalDate.now());
        userAdditionalService.setStatus(UserAdditionalService.UserAdditionalServiceStatus.CANCELLED);
        userAdditionalServiceRepository.save(userAdditionalService);
        
        log.info("부가서비스 해지 완료: userAddServiceId={}", userAddServiceId);
    }
    
    public List<UserAdditionalServiceResponse> getUserAdditionalServices(Long userId) {
        log.info("사용자 부가서비스 목록 조회 요청: userId={}", userId);
        
        List<UserAdditionalService> services = userAdditionalServiceRepository
                .findAllByUserIdOrderByStartDateDesc(userId);
        
        return services.stream()
                .map(UserAdditionalServiceResponse::from)
                .toList();
    }
    
    public List<UserAdditionalServiceResponse> getActiveUserAdditionalServices(Long userId) {
        log.info("사용자 활성 부가서비스 목록 조회 요청: userId={}", userId);
        
        List<UserAdditionalService> services = userAdditionalServiceRepository
                .findByUserIdAndStatusOrderByStartDateDesc(userId, UserAdditionalService.UserAdditionalServiceStatus.ACTIVE);
        
        return services.stream()
                .map(UserAdditionalServiceResponse::from)
                .toList();
    }
}
