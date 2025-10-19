package com.woodiny.my_awesome_repository.repository;

import com.woodiny.my_awesome_repository.entity.UserAdditionalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAdditionalServiceRepository extends JpaRepository<UserAdditionalService, Long> {
    
    @Query("SELECT uas FROM UserAdditionalService uas WHERE uas.user.userId = :userId ORDER BY uas.startDate DESC")
    List<UserAdditionalService> findAllByUserIdOrderByStartDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT uas FROM UserAdditionalService uas WHERE uas.user.userId = :userId AND uas.status = :status ORDER BY uas.startDate DESC")
    List<UserAdditionalService> findByUserIdAndStatusOrderByStartDateDesc(@Param("userId") Long userId, @Param("status") UserAdditionalService.UserAdditionalServiceStatus status);
    
    @Query("SELECT uas FROM UserAdditionalService uas WHERE uas.user.userId = :userId AND uas.additionalService.serviceId = :serviceId AND uas.status = 'ACTIVE'")
    Optional<UserAdditionalService> findActiveByUserIdAndServiceId(@Param("userId") Long userId, @Param("serviceId") String serviceId);
    
    boolean existsByUserUserIdAndAdditionalServiceServiceIdAndStatus(Long userId, String serviceId, UserAdditionalService.UserAdditionalServiceStatus status);
}
