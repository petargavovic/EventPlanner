package com.events.planner.dto;

import java.time.LocalDateTime;

public class ReservationHistoryDto {

    private Long id;
    private Long reservationId;
    private String action;
    private LocalDateTime changedAt;
    private String changedBy;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private String description;
    private Long userId;
    private Long hallId;
    private Long eventId;

    public ReservationHistoryDto() {
    }

    public ReservationHistoryDto(Long id, Long reservationId, String action,
            LocalDateTime changedAt, String changedBy, LocalDateTime start,
            LocalDateTime end, String status, String description,
            Long userId, Long hallId, Long eventId) {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return "ReservationHistoryDto{" + "id=" + id + ", reservationId=" + reservationId
                + ", action=" + action + ", changedAt=" + changedAt + ", changedBy=" + changedBy
                + ", start=" + start + ", end=" + end + ", status=" + status
                + ", description=" + description + ", userId=" + userId
                + ", hallId=" + hallId + ", eventId=" + eventId + '}';
    }
}
