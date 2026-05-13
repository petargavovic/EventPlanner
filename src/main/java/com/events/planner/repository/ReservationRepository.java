package com.events.planner.repository;

import com.events.planner.entity.Reservation;
import com.events.planner.entity.ReservationStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 *
 * @author MAU
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation>  {

    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    Page<Reservation> findByHallId(Long hallId, Pageable pageable);

    Page<Reservation> findByEventId(Long eventId, Pageable pageable);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
     @Query("""
           select case when count(r) > 0 then true else false end
           from Reservation r
           where r.hall.id = :hallId
             and r.start < :end
             and r.end > :start
             and r.status = :status
             and (:excludeId is null or r.id <> :excludeId)
           """)
    boolean existsHallReservationConflict(@Param("hallId") Long hallId,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("status") ReservationStatus status,
                                          @Param("excludeId") Long excludeId);
}
