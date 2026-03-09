package com.events.planner.repository;

import com.events.planner.entity.Event;
import com.events.planner.entity.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByType(EventType type, Pageable pageable);

    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Event> findBySubjectId(Long subjectId, Pageable pageable);
}
