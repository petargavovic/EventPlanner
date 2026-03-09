package com.events.planner.mapper.impl;

import com.events.planner.dto.SubjectDto;
import com.events.planner.entity.Subject;
import com.events.planner.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class SubjectDtoEntityMapper implements DtoEntityMapper<SubjectDto, Subject> {

    @Override
    public SubjectDto toDto(Subject e) {
        return new SubjectDto(
                e.getId(),
                e.getCode(),
                e.getName()
        );
    }

    @Override
    public Subject toEntity(SubjectDto t) {
        return new Subject(
                t.getId(),
                t.getCode(),
                t.getName()
        );
    }

    public void updateEntity(Subject entity, SubjectDto dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
    }

  
}