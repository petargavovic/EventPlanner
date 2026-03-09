package com.events.planner.repository;

import com.events.planner.entity.Reservation;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 *
 * @author MAU
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    Page<Reservation> findByHallId(Long hallId, Pageable pageable);

    Page<Reservation> findByEventId(Long eventId, Pageable pageable);

    Page<Reservation> findByStatusContainingIgnoreCase(String status, Pageable pageable);

    @Query("""
           select case when count(r) > 0 then true else false end
           from Reservation r
           where r.hall.id = :hallId
             and r.start < :end
             and r.end > :start
             and (:excludeId is null or r.id <> :excludeId)
           """)
    boolean existsHallReservationConflict(@Param("hallId") Long hallId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("excludeId") Long excludeId);
}
