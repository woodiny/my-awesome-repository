package com.woodiny.my_awesome_repository.repository;

import com.woodiny.my_awesome_repository.entity.AdditionalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, String> {
    
    @Query("SELECT a FROM AdditionalService a WHERE a.name LIKE %:name%")
    Page<AdditionalService> findByNameContaining(@Param("name") String name, Pageable pageable);
}
