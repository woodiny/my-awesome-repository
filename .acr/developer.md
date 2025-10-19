당신은 최고의 Developer 입니다.

Code Reviewer는 비즈니스 로직이나 아키텍처 관점이 아닌, “순수 코드 구현 수준”에서 **정확성**, **가독성**, **유지보수성**, **안전성**, **효율성** 등을 중심으로 리뷰하는 역할입니다.  
업무 요구사항, 보안, 설계, 코드 스타일 등은 다루지 않고, 그 역할의 전 단계로서 코드 자체의 오류, 구조, 클린 코드 원칙 위반, 코드 냄새(code smell) 등을 검토합니다.

API 명세서

공통 규칙
	•	요청/응답은 JSON 형식.  ￼
	•	URI는 명사(리소스)로 설계.  ￼
	•	컬렉션은 복수형 리소스명 사용 (/users, /plans 등)
	•	HTTP 메서드에 따라 행위 구분: POST(생성), GET(조회), PUT/PATCH(수정), DELETE(삭제)
	•	상태코드 적절히 사용: 201 Created, 200 OK, 204 No Content, 400/404/409 오류 등
	•	에러 응답에는 오류 코드, 메시지, 필드 정보 등을 담는 것이 좋음.  ￼

⸻

2.1 사용자 가입

POST /api/users
	•	설명: 신규 사용자 가입
	•	요청 바디:

{
  "name": "홍길동",
  "contactNumber": "010-1234-5678",
  "email": "hong@example.com"
}


	•	응답:
	•	상태코드: 201 Created
	•	바디 예시:

{
  "userId": 1001,
  "name": "홍길동",
  "contactNumber": "010-1234-5678",
  "email": "hong@example.com",
  "status": "ACTIVE",
  "registrationDate": "2025-10-19T12:34:56Z"
}


	•	오류 케이스:
	•	필수 필드 누락 → 400 Bad Request
	•	동일 contactNumber 또는 email이 이미 존재 → 409 Conflict

GET /api/users/{userId}
	•	설명: 특정 사용자 정보 조회
	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "userId": 1001,
  "name": "홍길동",
  "contactNumber": "010-1234-5678",
  "email": "hong@example.com",
  "status": "ACTIVE",
  "registrationDate": "2025-10-19T12:34:56Z"
}


	•	오류 케이스:
	•	userId가 존재하지 않을 경우 → 404 Not Found

⸻

2.2 요금제 가입 및 변경

POST /api/users/{userId}/plan
	•	설명: 사용자가 요금제 신규 가입
	•	요청 바디:

{
  "planId": "plan-basic-001",
  "effectiveFrom": "2025-10-20"
}


	•	응답:
	•	상태코드: 201 Created
	•	바디 예시:

{
  "userPlanId": 5001,
  "userId": 1001,
  "planId": "plan-basic-001",
  "startDate": "2025-10-20",
  "endDate": null,
  "status": "ACTIVE"
}


	•	오류 케이스:
	•	userId 존재 X 또는 status ≠ ACTIVE → 400 Bad Request or 403 Forbidden
	•	이미 활성 요금제가 있는 경우 → 409 Conflict
	•	planId 존재하지 않음 → 404 Not Found

PUT /api/users/{userId}/plan
	•	설명: 사용자의 요금제 변경
	•	요청 바디:

{
  "newPlanId": "plan-premium-002",
  "effectiveFrom": "2025-10-25"
}


	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "userPlanId": 6001,
  "userId": 1001,
  "planId": "plan-premium-002",
  "startDate": "2025-10-25",
  "endDate": null,
  "status": "ACTIVE"
}


	•	오류 케이스:
	•	userId 존재 X 또는 status ≠ ACTIVE → 400/403
	•	현재 활성 요금제가 없으면 변경 불가 → 409 Conflict
	•	newPlanId가 현재 planId와 동일한 경우 → 400 Bad Request
	•	newPlanId 존재하지 않음 → 404 Not Found

GET /api/users/{userId}/plan
	•	설명: 사용자의 현재 활성 요금제 조회
	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "userPlanId": 6001,
  "userId": 1001,
  "planId": "plan-premium-002",
  "startDate": "2025-10-25",
  "endDate": null,
  "status": "ACTIVE"
}


	•	오류 케이스:
	•	활성 요금제가 없으면 → 404 Not Found

⸻

2.3 부가서비스 가입·해지

POST /api/users/{userId}/additional-services
	•	설명: 사용자가 부가서비스 신규 가입
	•	요청 바디:

{
  "serviceId": "svc-roaming-001",
  "effectiveFrom": "2025-10-20"
}


	•	응답:
	•	상태코드: 201 Created
	•	바디 예시:

{
  "userAddServiceId": 7001,
  "userId": 1001,
  "serviceId": "svc-roaming-001",
  "startDate": "2025-10-20",
  "endDate": null,
  "status": "ACTIVE"
}


	•	오류 케이스:
	•	userId 존재 X 또는 status ≠ ACTIVE → 400/403
	•	해당 사용자가 활성 요금제 없음 → 400 Bad Request
	•	동일 serviceId로 이미 ACTIVE 상태라면 → 409 Conflict
	•	serviceId 존재하지 않음 → 404 Not Found

DELETE /api/users/{userId}/additional-services/{userAddServiceId}
	•	설명: 사용자의 부가서비스 해지
	•	응답:
	•	상태코드: 204 No Content
	•	오류 케이스:
	•	userId 또는 userAddServiceId 존재하지 않음 → 404 Not Found
	•	해당 부가서비스가 이미 해지(CANCELLED) 또는 endDate 설정된 경우 → 409 Conflict

GET /api/users/{userId}/additional-services
	•	설명: 사용자의 부가서비스 목록 조회 (현재 활성 또는 전체)
	•	쿼리 파라미터 예시:
	•	?status=ACTIVE (선택적)
	•	?cursor=<token>&limit=20 (커서 기반 페이징 고려)
	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "items": [
    {
      "userAddServiceId": 7001,
      "serviceId": "svc-roaming-001",
      "startDate": "2025-10-20",
      "endDate": null,
      "status": "ACTIVE"
    },
    {
      "userAddServiceId": 7002,
      "serviceId": "svc-data-topup-002",
      "startDate": "2025-10-22",
      "endDate": "2025-11-22",
      "status": "CANCELLED"
    }
  ],
  "nextCursor": "eyJvZmZzZXQiOjIwLCJsaW1pdCI6MjB9"
}


	•	오류 케이스:
	•	userId 존재하지 않음 → 404 Not Found

⸻

2.4 요금제 및 부가서비스 조회

GET /api/plans
	•	설명: 요금제(Plan) 목록 조회
	•	쿼리 파라미터 예시: ?limit=50&cursor=<token>, ?nameContains=Premium
	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "items": [
    {
      "planId": "plan-basic-001",
      "name": "Basic Plan",
      "monthlyFee": 30000,
      "includedVoiceMinutes": 100,
      "includedSmsCount": 100,
      "includedDataGB": 5,
      "description": "입문형 요금제"
    },
    {
      "planId": "plan-premium-002",
      "name": "Premium Plan",
      "monthlyFee": 60000,
      "includedVoiceMinutes": 300,
      "includedSmsCount": 300,
      "includedDataGB": 15,
      "description": "고급형 요금제"
    }
  ],
  "nextCursor": "..."
}


	•	오류 케이스: 별도 복잡한 오류 없음 (파라미터 오류 시 400 Bad Request)

GET /api/additional-services
	•	설명: 부가서비스(AdditionalService) 목록 조회
	•	쿼리 파라미터 예시: ?limit=50&cursor=<token>, ?nameContains=Roaming
	•	응답:
	•	상태코드: 200 OK
	•	바디 예시:

{
  "items": [
    {
      "serviceId": "svc-roaming-001",
      "name": "로밍 무제한",
      "monthlyFee": 20000,
      "description": "해외여행 시 로밍 사용 무제한"
    },
    {
      "serviceId": "svc-data-topup-002",
      "name": "데이터 충전팩",
      "monthlyFee": 10000,
      "description": "매월 5GB 추가 데이터 제공"
    }
  ],
  "nextCursor": "..."
}


	•	오류 케이스: 마찬가지로 파라미터 오류 시 400 Bad Request

⸻

3. 요금제 변경 시 유의사항 및 흐름
	•	사용자가 요금제를 변경할 경우, 기존 UserPlan 엔티티의 endDate를 변경 요청일의 전일로 설정하고 status = CANCELLED 처리한다.
	•	새 요금제 가입(POST 방식처럼 새로운 UserPlan 생성)과 같은 프로세스로 처리하며 startDate = effectiveFrom, status = ACTIVE로 한다.
	•	변경 요청 시 일할 계산(월 중 변경) 또는 부가서비스 호환성 체크(현재 보유 부가서비스가 새 요금제에서 허용되지 않을 경우 자동 해지 또는 에러 응답) 등이 비즈니스 로직으로 추가될 수 있다.
	•	위 흐름은 하나의 트랜잭션으로 처리되어야 한다(데이터 불일치 방지).
