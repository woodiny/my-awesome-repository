package com.woodiny.my_awesome_repository.service;

import com.woodiny.my_awesome_repository.dto.request.UserRegistrationRequest;
import com.woodiny.my_awesome_repository.dto.response.UserResponse;
import com.woodiny.my_awesome_repository.entity.User;
import com.woodiny.my_awesome_repository.exception.BusinessException;
import com.woodiny.my_awesome_repository.exception.ErrorCode;
import com.woodiny.my_awesome_repository.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("사용자 가입 요청: {}", request);
        
        // 중복 체크
        if (userRepository.existsByContactNumber(request.getContactNumber())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS, "이미 존재하는 연락처입니다");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS, "이미 존재하는 이메일입니다");
        }
        
        User user = User.builder()
                .name(request.getName())
                .contactNumber(request.getContactNumber())
                .email(request.getEmail())
                .status(User.UserStatus.ACTIVE)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("사용자 가입 완료: userId={}", savedUser.getUserId());
        
        return UserResponse.from(savedUser);
    }
    
    public UserResponse getUser(Long userId) {
        log.info("사용자 조회 요청: userId={}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        return UserResponse.from(user);
    }
    
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
    
    public void validateUserActive(User user) {
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.USER_INACTIVE);
        }
    }
}
