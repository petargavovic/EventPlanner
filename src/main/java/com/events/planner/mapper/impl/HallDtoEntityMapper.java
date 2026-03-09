package com.events.planner.mapper.impl;

import com.events.planner.dto.HallDto;
import com.events.planner.entity.Hall;
import com.events.planner.entity.HallType;
import com.events.planner.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class HallDtoEntityMapper implements DtoEntityMapper<HallDto, Hall> {

    @Override
    public HallDto toDto(Hall e) {
        return new HallDto(
                e.getId(),
                e.getName(),
                e.getCapacity(),
                e.getLocation(),
                e.getType() != null ? e.getType().name() : null,
                e.getEquipment()
        );
    }

    @Override
    public Hall toEntity(HallDto t) {
        return new Hall(
                t.getId(),
                t.getName(),
                t.getCapacity(),
                t.getLocation(),
                t.getType() != null ? HallType.valueOf(t.getType().trim().toUpperCase()) : null,
                t.getEquipment()
        );
    }

    public void updateEntity(Hall entity, HallDto dto) {
        entity.setName(dto.getName());
        entity.setCapacity(dto.getCapacity());
        entity.setLocation(dto.getLocation());
        entity.setType(dto.getType() != null ? HallType.valueOf(dto.getType().trim().toUpperCase()) : null);
        entity.setEquipment(dto.getEquipment());
    }
}
