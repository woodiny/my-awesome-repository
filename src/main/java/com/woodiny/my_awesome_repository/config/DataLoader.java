package com.woodiny.my_awesome_repository.config;

import com.woodiny.my_awesome_repository.entity.AdditionalService;
import com.woodiny.my_awesome_repository.entity.Plan;
import com.woodiny.my_awesome_repository.repository.AdditionalServiceRepository;
import com.woodiny.my_awesome_repository.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final PlanRepository planRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("초기 데이터 로딩 시작");
        
        loadPlans();
        loadAdditionalServices();
        
        log.info("초기 데이터 로딩 완료");
    }
    
    private void loadPlans() {
        if (planRepository.count() == 0) {
            log.info("요금제 초기 데이터 생성");
            
            Plan basicPlan = Plan.builder()
                    .planId("plan-basic-001")
                    .name("Basic Plan")
                    .monthlyFee(30000)
                    .includedVoiceMinutes(100)
                    .includedSmsCount(100)
                    .includedDataGB(5)
                    .description("입문형 요금제")
                    .build();
            
            Plan premiumPlan = Plan.builder()
                    .planId("plan-premium-002")
                    .name("Premium Plan")
                    .monthlyFee(60000)
                    .includedVoiceMinutes(300)
                    .includedSmsCount(300)
                    .includedDataGB(15)
                    .description("고급형 요금제")
                    .build();
            
            Plan unlimitedPlan = Plan.builder()
                    .planId("plan-unlimited-003")
                    .name("Unlimited Plan")
                    .monthlyFee(100000)
                    .includedVoiceMinutes(999999)
                    .includedSmsCount(999999)
                    .includedDataGB(50)
                    .description("무제한 요금제")
                    .build();
            
            planRepository.save(basicPlan);
            planRepository.save(premiumPlan);
            planRepository.save(unlimitedPlan);
            
            log.info("요금제 초기 데이터 생성 완료: {} 개", planRepository.count());
        }
    }
    
    private void loadAdditionalServices() {
        if (additionalServiceRepository.count() == 0) {
            log.info("부가서비스 초기 데이터 생성");
            
            AdditionalService roamingService = AdditionalService.builder()
                    .serviceId("svc-roaming-001")
                    .name("로밍 무제한")
                    .monthlyFee(20000)
                    .description("해외여행 시 로밍 사용 무제한")
                    .build();
            
            AdditionalService dataTopupService = AdditionalService.builder()
                    .serviceId("svc-data-topup-002")
                    .name("데이터 충전팩")
                    .monthlyFee(10000)
                    .description("매월 5GB 추가 데이터 제공")
                    .build();
            
            AdditionalService voiceService = AdditionalService.builder()
                    .serviceId("svc-voice-003")
                    .name("음성 무제한")
                    .monthlyFee(15000)
                    .description("음성통화 무제한")
                    .build();
            
            AdditionalService smsService = AdditionalService.builder()
                    .serviceId("svc-sms-004")
                    .name("문자 무제한")
                    .monthlyFee(5000)
                    .description("문자메시지 무제한")
                    .build();
            
            AdditionalService premiumDataService = AdditionalService.builder()
                    .serviceId("svc-premium-data-005")
                    .name("프리미엄 데이터")
                    .monthlyFee(30000)
                    .description("매월 20GB 추가 데이터 제공")
                    .build();
            
            additionalServiceRepository.save(roamingService);
            additionalServiceRepository.save(dataTopupService);
            additionalServiceRepository.save(voiceService);
            additionalServiceRepository.save(smsService);
            additionalServiceRepository.save(premiumDataService);
            
            log.info("부가서비스 초기 데이터 생성 완료: {} 개", additionalServiceRepository.count());
        }
    }
}
