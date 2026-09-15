package com.events.planner.service.impl;

import com.events.planner.dto.EventDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.EventType;
import com.events.planner.entity.Subject;
import com.events.planner.mapper.impl.EventDtoEntityMapper;
import com.events.planner.repository.EventRepository;
import com.events.planner.repository.SubjectRepository;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.expectThrows;

public class EventServiceImplTest {

    private EventRepository eventRepository;
    private SubjectRepository subjectRepository;
    private EventServiceImpl eventService;

    @BeforeMethod
    public void setUp() {
        eventRepository = mock(EventRepository.class);
        subjectRepository = mock(SubjectRepository.class);
        eventService = new EventServiceImpl(eventRepository, subjectRepository, new EventDtoEntityMapper());
    }

    @Test
    public void shouldCreateEventWithSubject() throws Exception {
        Subject subject = new Subject(5L, "ARS", "Automation");
        EventDto dto = new EventDto(null, "Lecture", "LECTURE", "Intro lecture", 50, 5L);

        when(subjectRepository.findById(5L)).thenReturn(Optional.of(subject));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId(10L);
            return event;
        });

        EventDto result = eventService.create(dto);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(captor.capture());

        assertEquals(result.getId(), Long.valueOf(10L));
        assertEquals(result.getSubjectId(), Long.valueOf(5L));
        assertEquals(captor.getValue().getType(), EventType.LECTURE);
        assertEquals(captor.getValue().getSubject(), subject);
    }

    @Test
    public void shouldReturnEventById() throws Exception {
        Event event = new Event(10L, "Lecture", EventType.LECTURE, "Intro", 50, null);
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        EventDto result = eventService.getById(10L);

        assertEquals(result.getId(), Long.valueOf(10L));
        assertEquals(result.getName(), "Lecture");
    }

    @Test
    public void shouldClearSubjectWhenEventIsUpdatedWithoutSubject() throws Exception {
        Subject oldSubject = new Subject(5L, "ARS", "Automation");
        Event existing = new Event(10L, "Old event", EventType.MEETING, "Old", 20, oldSubject);
        EventDto update = new EventDto(10L, "Updated event", "WORKSHOP", "Updated", 40, null);

        when(eventRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventDto result = eventService.update(10L, update);

        assertEquals(result.getType(), "WORKSHOP");
        assertEquals(result.getCapacity(), 40);
        assertNull(result.getSubjectId());
        assertNull(existing.getSubject());
        verify(eventRepository).save(existing);
    }

    @Test
    public void shouldRejectEventWithoutName() {
        EventDto dto = new EventDto(null, null, "LECTURE", "Intro", 50, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Event name is required.");
        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    public void shouldRejectBlankEventName() {
        EventDto dto = new EventDto(null, "   ", "LECTURE", "Intro", 50, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Event name is required.");
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void shouldRejectEventWithoutType() {
        EventDto dto = new EventDto(null, "Lecture", null, "Intro", 50, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Event type is required.");
        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    public void shouldRejectBlankEventType() {
        EventDto dto = new EventDto(null, "Lecture", "   ", "Intro", 50, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Event type is required.");
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void shouldRejectEventWithNegativeCapacity() {
        EventDto dto = new EventDto(null, "Lecture", "LECTURE", "Intro", -1, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Capacity cannot be negative.");
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void shouldRejectInvalidEventType() {
        EventDto dto = new EventDto(null, "Lecture", "INVALID", "Intro lecture", 50, null);

        Exception exception = expectThrows(Exception.class, () -> eventService.create(dto));

        assertEquals(exception.getMessage(), "Invalid event type.");
        verify(eventRepository, never()).save(any(Event.class));
    }
}
