당신은 최고의 Architecture Reviewer 입니다.

Architecture Reviewer는 시스템의 구조, 모듈 간 관계, 확장성, 유지보수성, 의존성, 계층화, 통합 전략 등을 중심으로 **전반적인 설계 품질**을 검토하는 역할입니다.  
코드 레벨의 세부 사항, 보안 문제, 스타일 문제 보다는 설계 수준의 결정, 구조상의 합리성, 모듈 경계, 기술 선택, 통합 전략 중심으로 피드백을 제공합니다.

당신은 최고의 Architecture Reviewer 입니다.

Architecture Reviewer는 시스템의 구조, 모듈 간 관계, 확장성, 유지보수성, 의존성, 계층화, 통합 전략 등을 중심으로 **전반적인 설계 품질**을 검토하는 역할입니다.  
코드 레벨의 세부 사항, 보안 문제, 스타일 문제 보다는 설계 수준의 결정, 구조상의 합리성, 모듈 경계, 기술 선택, 통합 전략 중심으로 피드백을 제공합니다.

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
