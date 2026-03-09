/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.service.impl;

import com.events.planner.dto.EventDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.EventType;
import com.events.planner.entity.Subject;
import com.events.planner.mapper.impl.EventDtoEntityMapper;
import com.events.planner.repository.EventRepository;
import com.events.planner.repository.SubjectRepository;
import com.events.planner.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author MAU
 */
@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final SubjectRepository subjectRepository;
    private final EventDtoEntityMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository, SubjectRepository subjectRepository, EventDtoEntityMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.subjectRepository = subjectRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventDto create(EventDto dto) throws Exception {
  if (dto.getName() == null || dto.getName().isBlank()) {
            throw new Exception("Event name is required.");
        }
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new Exception("Event type is required.");
        }
        if (dto.getCapacity() < 0) {
            throw new Exception("Capacity cannot be negative.");
        }

        parseEventType(dto.getType());

        Event event = eventMapper.toEntity(dto);

        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new Exception("Subject not found."));
            event.setSubject(subject);
        }

        Event saved = eventRepository.save(event);
        return eventMapper.toDto(saved);    }

    @Override
    public EventDto getById(Long id) throws Exception {
  return eventRepository.findById(id)
                .map(eventMapper::toDto)
                .orElseThrow(() -> new Exception("Event not found."));    }

      @Override
    public Page<EventDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return eventRepository.findAll(pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventDto> getByType(String type, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        EventType eventType = parseEventType(type);
        return eventRepository.findByType(eventType, pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventDto> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return eventRepository.findByNameContainingIgnoreCase(name, pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventDto> getBySubjectId(Long subjectId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return eventRepository.findBySubjectId(subjectId, pageable).map(eventMapper::toDto);
    }

    @Override
    public EventDto update(Long id, EventDto dto) throws Exception {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new Exception("Event not found."));

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new Exception("Event name is required.");
        }
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new Exception("Event type is required.");
        }
        if (dto.getCapacity() < 0) {
            throw new Exception("Capacity cannot be negative.");
        }

        parseEventType(dto.getType());

        eventMapper.updateEntity(event, dto);

        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new Exception("Subject not found."));
            event.setSubject(subject);
        } else {
            event.setSubject(null);
        }

        Event saved = eventRepository.save(event);
        return eventMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (!eventRepository.existsById(id)) {
            throw new Exception("Event not found.");
        }
        eventRepository.deleteById(id);
    }

     private EventType parseEventType(String type) throws Exception {
        try {
            return EventType.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            throw new Exception("Invalid event type.");
        }
    }
    
    
    
}
