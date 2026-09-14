package com.events.planner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_history")
public class ReservationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ReservationHistoryAction action;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "hall_id", nullable = false)
    private Long hallId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    public ReservationHistory() {
    }

    public ReservationHistory(Long id, Long reservationId, ReservationHistoryAction action,
            LocalDateTime changedAt, String changedBy, LocalDateTime start, LocalDateTime end,
            ReservationStatus status, String description, Long userId, Long hallId, Long eventId) {
        this.id = id;
        this.reservationId = reservationId;
        this.action = action;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.start = start;
        this.end = end;
        this.status = status;
        this.description = description;
        this.userId = userId;
        this.hallId = hallId;
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationHistoryAction getAction() {
        return action;
    }

    public void setAction(ReservationHistoryAction action) {
        this.action = action;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "ReservationHistory{" + "id=" + id + ", reservationId=" + reservationId
                + ", action=" + action + ", changedAt=" + changedAt + ", changedBy=" + changedBy
                + ", start=" + start + ", end=" + end + ", status=" + status
                + ", description=" + description + ", userId=" + userId
                + ", hallId=" + hallId + ", eventId=" + eventId + '}';
    }
}
