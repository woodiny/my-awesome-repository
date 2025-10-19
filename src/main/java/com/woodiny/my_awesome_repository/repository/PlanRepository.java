package com.woodiny.my_awesome_repository.repository;

import com.woodiny.my_awesome_repository.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    
    @Query("SELECT p FROM Plan p WHERE p.name LIKE %:name%")
    Page<Plan> findByNameContaining(@Param("name") String name, Pageable pageable);
}
