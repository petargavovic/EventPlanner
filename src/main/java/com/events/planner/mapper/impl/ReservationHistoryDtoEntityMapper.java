package com.events.planner.mapper.impl;

import com.events.planner.dto.ReservationHistoryDto;
import com.events.planner.entity.ReservationHistory;
import com.events.planner.entity.ReservationHistoryAction;
import com.events.planner.entity.ReservationStatus;
import com.events.planner.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationHistoryDtoEntityMapper
        implements DtoEntityMapper<ReservationHistoryDto, ReservationHistory> {

    @Override
    public ReservationHistoryDto toDto(ReservationHistory entity) {
        return new ReservationHistoryDto(
                entity.getId(),
                entity.getReservationId(),
                entity.getAction() != null ? entity.getAction().name() : null,
                entity.getChangedAt(),
                entity.getChangedBy(),
                entity.getStart(),
                entity.getEnd(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getDescription(),
                entity.getUserId(),
                entity.getHallId(),
                entity.getEventId()
        );
    }

    @Override
    public ReservationHistory toEntity(ReservationHistoryDto dto) {
        return new ReservationHistory(
                dto.getId(),
                dto.getReservationId(),
                dto.getAction() != null
                        ? ReservationHistoryAction.valueOf(dto.getAction().trim().toUpperCase())
                        : null,
                dto.getChangedAt(),
                dto.getChangedBy(),
                dto.getStart(),
                dto.getEnd(),
                dto.getStatus() != null
                        ? ReservationStatus.valueOf(dto.getStatus().trim().toUpperCase())
                        : null,
                dto.getDescription(),
                dto.getUserId(),
                dto.getHallId(),
                dto.getEventId()
        );
    }
}
