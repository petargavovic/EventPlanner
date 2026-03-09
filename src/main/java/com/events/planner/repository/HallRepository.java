package com.events.planner.repository;

import com.events.planner.entity.Hall;
import com.events.planner.entity.HallType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author MAU
 */
@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    

    Page<Hall> findByType (HallType type, Pageable pageable);

    Page<Hall> findByNameContainingIgnoreCase(String type, Pageable pageable);

    Page<Hall> findByCapacityGreaterThanEqual(int capacity, Pageable pageable);
}