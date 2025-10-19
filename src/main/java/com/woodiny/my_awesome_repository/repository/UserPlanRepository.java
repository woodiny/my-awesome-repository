package com.woodiny.my_awesome_repository.repository;

import com.woodiny.my_awesome_repository.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    
    @Query("SELECT up FROM UserPlan up WHERE up.user.userId = :userId AND up.status = 'ACTIVE'")
    Optional<UserPlan> findActiveByUserId(@Param("userId") Long userId);
    
    @Query("SELECT up FROM UserPlan up WHERE up.user.userId = :userId ORDER BY up.startDate DESC")
    List<UserPlan> findAllByUserIdOrderByStartDateDesc(@Param("userId") Long userId);
    
    boolean existsByUserUserIdAndStatus(Long userId, UserPlan.UserPlanStatus status);
}
