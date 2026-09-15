package com.events.planner.repository;

import com.events.planner.entity.ReservationHistory;
import com.events.planner.entity.ReservationHistoryAction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    List<ReservationHistory> findByReservationIdOrderByChangedAtAscIdAsc(Long reservationId);

    List<ReservationHistory> findByReservationIdAndActionOrderByChangedAtAscIdAsc(
            Long reservationId,
            ReservationHistoryAction action);
}
