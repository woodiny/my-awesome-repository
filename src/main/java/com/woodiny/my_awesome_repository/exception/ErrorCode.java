package com.woodiny.my_awesome_repository.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // 사용자 관련 에러
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS("U002", "이미 존재하는 사용자입니다"),
    USER_INACTIVE("U003", "비활성 사용자입니다"),
    
    // 요금제 관련 에러
    PLAN_NOT_FOUND("P001", "요금제를 찾을 수 없습니다"),
    PLAN_ALREADY_SUBSCRIBED("P002", "이미 활성 요금제가 있습니다"),
    PLAN_NOT_SUBSCRIBED("P003", "활성 요금제가 없습니다"),
    PLAN_SAME_SUBSCRIPTION("P004", "동일한 요금제로 변경할 수 없습니다"),
    
    // 부가서비스 관련 에러
    ADDITIONAL_SERVICE_NOT_FOUND("A001", "부가서비스를 찾을 수 없습니다"),
    ADDITIONAL_SERVICE_ALREADY_SUBSCRIBED("A002", "이미 가입된 부가서비스입니다"),
    ADDITIONAL_SERVICE_NOT_SUBSCRIBED("A003", "가입되지 않은 부가서비스입니다"),
    ADDITIONAL_SERVICE_REQUIRES_PLAN("A004", "부가서비스 가입을 위해서는 활성 요금제가 필요합니다"),
    
    // 공통 에러
    INVALID_REQUEST("C001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("C002", "서버 내부 오류가 발생했습니다"),
    VALIDATION_ERROR("C003", "입력값 검증 오류입니다");
    
    private final String code;
    private final String message;
}
