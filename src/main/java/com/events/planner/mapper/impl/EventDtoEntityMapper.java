package com.events.planner.mapper.impl;

import com.events.planner.dto.EventDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.EventType;
import com.events.planner.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class EventDtoEntityMapper implements DtoEntityMapper<EventDto, Event> {

    @Override
    public EventDto toDto(Event e) {
        return new EventDto(
                e.getId(),
                e.getName(),
                e.getType() != null ? e.getType().name() : null,
                e.getDescription(),
                e.getCapacity(),
                e.getSubject() != null ? e.getSubject().getId() : null
        );
    }

    @Override
    public Event toEntity(EventDto t) {
        return new Event(
                t.getId(),
                t.getName(),
                t.getType() != null ? EventType.valueOf(t.getType().trim().toUpperCase()) : null,
                t.getDescription(),
                t.getCapacity(),
                null
        );
    }

    public void updateEntity(Event entity, EventDto dto) {
        entity.setName(dto.getName());
        entity.setType(dto.getType() != null ? EventType.valueOf(dto.getType().trim().toUpperCase()) : null);
        entity.setDescription(dto.getDescription());
        entity.setCapacity(dto.getCapacity());
    }
}