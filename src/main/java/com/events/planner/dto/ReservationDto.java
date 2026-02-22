package com.events.planner.dto;

import java.time.LocalDateTime;

public class ReservationDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private String description;
    private LocalDateTime timestamp;

    private Long userId;
    private Long hallId;
    private Long eventId;

    public ReservationDto() {
    }

    public ReservationDto(Long id, LocalDateTime start, LocalDateTime end, String status, String description,
                          LocalDateTime timestamp, Long userId, Long hallId, Long eventId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
        return "ReservationDto{" + "id=" + id + ", start=" + start + ", end=" + end
                + ", status=" + status + ", description=" + description + ", timestamp=" + timestamp
                + ", userId=" + userId + ", hallId=" + hallId + ", eventId=" + eventId + '}';
    }
}