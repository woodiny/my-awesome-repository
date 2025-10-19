당신은 최고의 Business Reviewer (비즈니스 / 요구사항 관점 리뷰어) 입니다.

Business Reviewer는 코드 변경(혹은 기능 추가/수정)이 “요구사항” 또는 “비즈니스 목적”에 부합하는지를 확인하고, 모호성·불일치·누락·비즈니스 로직 오류 등을 찾아내는 역할을 담당합니다.  
기술적 구현, 알고리즘, 최적화, 보안, 코드 스타일은 다루지 않고, 설계 요청서/스펙/도메인 관점 중심으로만 검토합니다.

좋습니다. 아래는 KT 통신 비즈니스를 위한 백엔드 애플리케이션 요구사항입니다. 이후 이 요구사항을 기반으로 ERD, 전체 API 설계, 비즈니스 규칙, 예외 케이스 등을 상세히 작성해 나가면 좋겠습니다. (코드는 필요 없고 구조화된 요구사항/사양서 형태입니다.)

⸻

1. 개요

본 애플리케이션은 KT 통신사업자가 **사용자(개인 고객)**에게 통신 서비스를 제공하는 과정에서 다음 업무를 지원합니다:
	•	사용자 신규 가입
	•	요금제(Plan) 가입 및 변경
	•	부가서비스(Additional Service) 가입 및 해지
요금제 및 부가서비스는 사용자의 가입 이후에만 가능하며, 사용자는 하나의 요금제만 보유할 수 있고 여러 개의 부가서비스는 동시에 가입 가능해야 합니다.

⸻

2. 주요 개념 및 용어 정의
	•	사용자(User / Customer): 통신 서비스를 이용하고자 KT에 가입한 개인 고객.
	•	요금제(Plan / Tariff): 통신서비스 이용을 위해 고객이 선택하는 기본 요금모델. 사용자는 한 번에 하나의 요금제만 가입할 수 있습니다.
	•	부가서비스(AdditionalService / OptionalService): 기본 요금제 외에 선택할 수 있는 추가 서비스. 사용자는 여러 개의 부가서비스에 가입 가능하며, 각 부가서비스는 개별적으로 가입·해지할 수 있습니다.
	•	가입(Sign-up / Subscription): 사용자가 통신서비스를 새로 신청하는 것. 사용자 상태가 “가입됨”이 되어야 요금제나 부가서비스 업무가 가능해집니다.
	•	변경(Change / Modification): 이미 가입된 요금제나 부가서비스를 다른 상태로 변경하는 것 (예: 요금제를 다른 요금제로 바꾸거나, 부가서비스를 해지).
	•	해지(Unsubscribe / Cancellation): 부가서비스 등에서 사용자가 해당 서비스를 중단하는 것. 요금제의 해지나 사용자 전체 서비스 해지는 본 요구사항에서는 별도 고려할 수 있습니다.

⸻

3. 요구사항 – 기능적 측면

다음은 시스템이 제공해야 하는 기능들입니다.

3.1 사용자 가입
	•	사용자는 신규로 가입 요청을 할 수 있다.
	•	가입 시 필요한 입력사항: 사용자 식별 정보(예: 사용자ID, 이름, 연락처 등) 및 서비스 계약 정보(가입일, 상태 등).
	•	가입이 완료되면 사용자 상태가 “활성(Active)” 상태가 된다.
	•	가입이 되지 않은 사용자(즉 서비스 계약이 없는 사용자)는 요금제 가입이나 부가서비스 가입을 할 수 없다.

3.2 요금제 업무
	•	사용자는 현재 가입된 요금제가 없을 경우 요금제에 가입할 수 있다.
	•	사용자는 요금제를 변경할 수 있다. 즉, 기존 요금제를 해지하고 다른 요금제로 전환한다.
	•	사용자가 요금제에 가입하려면 사용자 가입이 먼저 완료되어 있어야 한다.
	•	한 사용자는 동시에 오직 하나의 요금제만 보유할 수 있다. (즉 요금제 가입 시 기존 요금제가 있다면 먼저 변경 혹은 해지 절차가 내부적으로 이루어져야 한다.)
	•	요금제 변경 시 고려사항: 변경 요청일 기준으로 기존 요금제의 잔여일/일할계산, 새 요금제 적용 등. (예: 월 중 요금제 변경시 일할 계산 필요)  ￼
	•	요금제 정보에는 최소 다음이 포함된다: 요금제 ID, 이름, 월정액, 기본제공 음성/문자/데이터량 등.

3.3 부가서비스 업무
	•	사용자는 부가서비스를 신규 가입할 수 있다.
	•	사용자는 부가서비스를 해지할 수 있다.
	•	사용자는 부가서비스를 여러 개 동시에 보유할 수 있다.
	•	사용자가 부가서비스에 가입하려면 사용자가 가입되어 있어야 하고 요금제에 가입된 상태가거나 요금제 가입이 필수라고 명시된다. (요금제가 없는 사용자의 부가서비스 가입은 허용하지 않는다.)
	•	부가서비스 가입 시 고려사항: 가입일, 월정액 여부, 일할계산, 가입 조건(예: 특정 요금제 이상에서만 가능) 등이 있을 수 있다.
	•	부가서비스 해지 시 고려사항: 해지 시점 이후 제공 중단, 해지 수수료 여부, 잔여기간 일할 환산 등.

⸻

4. 요구사항 – 비기능 및 제약 사항
	•	데이터 일관성 보장: 사용자 → 요금제 → 부가서비스 관계는 일관성 있어야 한다. 예를 들어, 사용자가 요금제를 변경했을 때 기존 부가서비스가 변경된 요금제에서 허용되지 않을 경우 자동으로 해지되거나 사용자에게 알림이 있어야 한다.
	•	트랜잭션 처리: 요금제 변경과 같은 중요한 업무는 트랜잭션으로 처리되어야 한다 (즉, 어중간한 중간 상태로 남지 않아야 한다).
	•	상태 변경 이력관리: 사용자 가입, 요금제 변경, 부가서비스 가입/해지 이벤트 등에 대해 이력을 남기는 것이 좋다.
	•	REST API 및 Cursor 기반 처리를 고려할 경우, 페이징 처리, 조회 속도, 대량 데이터 처리 등을 염두에 둔다.
	•	예외 처리: 존재하지 않는 사용자, 요금제, 부가서비스 가입 요청, 이미 가입된 요금제 중복 가입 요청, 허용되지 않는 요금제 변경 요청 등을 명확히 처리해야 한다.
	•	인증/인가: 실사용 환경이라면 사용자 인증/인가 메커니즘 필요하나 본 테스트 앱에서는 단순화 가능하다.

⸻

5. 엔티티 및 관계 제안

아래는 기본 엔티티와 관계를 정의한 제안입니다.
	•	User
	•	userId (PK)
	•	name
	•	contactNumber
	•	email
	•	status (예: ACTIVE, INACTIVE)
	•	registrationDate
	•	Plan
	•	planId (PK)
	•	name
	•	monthlyFee
	•	includedVoiceMinutes
	•	includedSmsCount
	•	includedDataGB
	•	description
	•	UserPlan (사용자-요금제 가입 정보)
	•	userPlanId (PK)
	•	userId (FK → User)
	•	planId (FK → Plan)
	•	startDate
	•	endDate (nullable: 현재 요금제면 null)
	•	status (예: ACTIVE, CANCELLED)
	•	AdditionalService
	•	serviceId (PK)
	•	name
	•	monthlyFee
	•	description
	•	UserAdditionalService (사용자-부가서비스 가입 정보)
	•	userAddServiceId (PK)
	•	userId (FK → User)
	•	serviceId (FK → AdditionalService)
	•	startDate
	•	endDate (nullable: 현재 가입이면 null)
	•	status (예: ACTIVE, CANCELLED)
	•	AuditLog (선택)
	•	logId (PK)
	•	entityType (e.g., “User”, “UserPlan”, “UserAdditionalService”)
	•	entityId
	•	action (CREATE, UPDATE, DELETE)
	•	actionDate
	•	changedBy (user or system)
	•	changeDetail

관계 요약
	•	User 1:N UserPlan
	•	User 1:N UserAdditionalService
	•	Plan 1:N UserPlan
	•	AdditionalService 1:N UserAdditionalService

제약사항
	•	UserPlan: 한 User에 대해 활성(Active)인 UserPlan이 하나만 존재해야 한다.
	•	UserAdditionalService: 한 User가 여러 UserAdditionalService를 가질 수 있다.
	•	UserAdditionalService 가입 시 UserPlan이 Active 상태여야 한다 (요금제가 있어야 부가서비스가 가입 가능).
	•	Plan 변경 시 이전 UserPlan의 endDate를 설정하고 status를 CANCELLED로, 새로운 UserPlan을 생성하여 status는 ACTIVE로 설정한다.

⸻

6. API 설계 제안

아래는 주요 기능을 위한 REST-API 엔드포인트 제안입니다. HTTP status codes, Request/Response 구조, 주요 유효성 검사를 포함합니다.

6.1 사용자 가입
	•	POST /api/users
	•	요청 바디 예시:

{
  "name": "홍길동",
  "contactNumber": "010-1234-5678",
  "email": "hong@example.com"
}


	•	응답: 생성된 사용자 정보 (userId 포함)
	•	유효성 검사: name, contactNumber 필수; 동일 contactNumber 또는 email 이미 존재 시 에러(409 Conflict)
	•	상태코드: 201 Created

	•	GET /api/users/{userId}
	•	사용자 정보 조회
	•	상태코드: 200 OK 또는 404 Not Found

6.2 요금제 가입 및 변경
	•	POST /api/users/{userId}/plan
	•	사용자에 요금제 신규 가입
	•	요청 바디 예시:

{
  "planId": "plan-basic-001",
  "effectiveFrom": "2025-10-19"
}


	•	유효성 검사: userId 존재·가입 상태여야 함; 해당 user에 이미 활성 요금제가 있으면 에러(409 Conflict) 또는 내부적으로 기존 요금제 종료 후 신규 가입 처리
	•	응답: 가입된 UserPlan 정보
	•	상태코드: 201 Created

	•	PUT /api/users/{userId}/plan
	•	사용자 요금제 변경
	•	요청 바디 예시:

{
  "newPlanId": "plan-premium-002",
  "effectiveFrom": "2025-10-20"
}


	•	유효성 검사: userId 존재·가입 상태여야 함; 현재 활성 요금제가 있어야 변경 가능; newPlanId는 기존과 달라야 함
	•	내부 처리: 기존 UserPlan의 endDate를 effectiveFrom-1일로 설정하고 status=CANCELLED; 신규 UserPlan 생성 with startDate=effectiveFrom, status=ACTIVE
	•	응답: 새로운 UserPlan 정보
	•	상태코드: 200 OK

	•	GET /api/users/{userId}/plan
	•	사용자의 현재 활성 요금제 조회
	•	상태코드: 200 OK or 404 if none

6.3 부가서비스 가입·해지
	•	POST /api/users/{userId}/additional-services
	•	요청 바디 예시:

{
  "serviceId": "svc-roaming-001",
  "effectiveFrom": "2025-10-19"
}


	•	유효성 검사: userId 존재·가입 상태여야 함; user에 활성 요금제가 있어야 함; 동일 serviceId로 이미 Active 상태 서비스가 있다면 에러(409 Conflict)
	•	응답: 생성된 UserAdditionalService 정보
	•	상태코드: 201 Created

	•	DELETE /api/users/{userId}/additional-services/{userAddServiceId}
	•	부가서비스 해지
	•	유효성 검사: userId & userAddServiceId 존재; 해당 서비스 status=ACTIVE여야 함
	•	처리: UserAdditionalService의 endDate = 해지일, status = CANCELLED
	•	응답: 204 No Content
	•	GET /api/users/{userId}/additional-services
	•	사용자의 모든 (과거+현재) 부가서비스 목록 또는 옵션으로 활성만 조회
	•	응답: 리스트
	•	상태코드: 200 OK

⸻

7. 비즈니스 시나리오 및 예외 처리

7.1 시나리오 예
	1.	사용자 A가 신규 가입한다 → status=ACTIVE.
	2.	사용자 A는 요금제 Basic에 가입한다.
	3.	사용자 A는 Premium 요금제로 변경한다. 기존 Basic요금제 종료, Premium요금제 활성화.
	4.	사용자 A는 부가서비스 “로밍 무제한”에 가입한다.
	5.	이후 사용자 A가 부가서비스 “데이터충전”에도 가입한다. → 여러 부가서비스 동시 보유 가능.
	6.	사용자 A가 “로밍 무제한”을 해지한다. → 해당 내역 endDate 설정, status=CANCELLED.
	7.	사용자 A가 요금제 변경 시, Premium 요금제에서 허용되지 않는 부가서비스가 있으면 시스템이 해당 부가서비스를 자동 해지하거나 경고를 띄운다.

7.2 주요 예외 및 검증
	•	사용자 가입되지 않은 상태에서 요금제 가입 요청 → 400 Bad Request 또는 403 Forbidden.
	•	사용자에게 이미 활성 요금제가 있는데 다시 같은 요금제로 신규 가입 요청 → 409 Conflict.
	•	사용자 요금제 없음 상태에서 부가서비스 가입 요청 → 400 Bad Request.
	•	부가서비스 가입 시 이미 동일 서비스가 활성인 경우 → 409 Conflict.
	•	부가서비스 해지 시 해당 가입 내역이 존재하지 않거나 이미 해지된 경우 → 404 Not Found 또는 409 Conflict.
	•	요금제 변경 시 newPlanId가 현재 요금제와 동일한 경우 → 400 Bad Request.
	•	요금제나 부가서비스 입력값 유효성 미달(예: 존재하지 않는 planId/serviceId) → 404 Not Found.

좋습니다. 아래는 앞서 정리한 요구사항을 바탕으로 ERD 설계안과 API 명세서입니다. 이후 필요하시면 Swagger (OpenAPI) 형식으로도 만들어 드릴 수 있어요.

⸻

1. ERD 설계

엔티티 및 속성

엔티티명	속성	비고
User	- user_id (PK)  - name  - contact_number  - email  - status (예: ACTIVE, INACTIVE)  - registration_date	사용자 신규가입 정보
Plan	- plan_id (PK)  - name  - monthly_fee  - included_voice_minutes  - included_sms_count  - included_data_gb  - description	요금제 정보
UserPlan	- user_plan_id (PK)  - user_id (FK → User)  - plan_id (FK → Plan)  - start_date  - end_date (nullable)  - status (예: ACTIVE, CANCELLED)	사용자의 요금제 가입／변경 정보
AdditionalService	- service_id (PK)  - name  - monthly_fee  - description	부가서비스 정보
UserAdditionalService	- user_add_service_id (PK)  - user_id (FK → User)  - service_id (FK → AdditionalService)  - start_date  - end_date (nullable)  - status (예: ACTIVE, CANCELLED)	사용자의 부가서비스 가입／해지 정보
AuditLog (선택)	- log_id (PK)  - entity_type  - entity_id  - action (CREATE, UPDATE, DELETE)  - action_date  - changed_by  - change_detail	변경 이력 관리용

관계 및 제약사항
	•	User 1 : N UserPlan
	•	UserPlan N : 1 Plan
	•	User 1 : N UserAdditionalService
	•	UserAdditionalService N : 1 AdditionalService
	•	제약: 한 사용자는 동시에 활성(Active)인 UserPlan을 오직 하나만 가질 수 있어야 함.
	•	부가서비스(UserAdditionalService) 가입 시 해당 사용자가 요금제(UserPlan) 상태가 ACTIVE여야 가입 가능.
	•	요금제 변경 시 기존 UserPlan의 end_date 설정 및 status 변경 → 신규 UserPlan 생성.

ERD 다이어그램 요약

User ──< UserPlan >── Plan  
User ──< UserAdditionalService >── AdditionalService  

각 엔티티는 위 표 속성을 갖습니다.

⸻

2. API 명세서

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
