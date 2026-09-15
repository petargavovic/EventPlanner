package com.events.planner.service.impl;

import com.events.planner.dto.SubjectDto;
import com.events.planner.entity.Subject;
import com.events.planner.mapper.impl.SubjectDtoEntityMapper;
import com.events.planner.repository.SubjectRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

public class SubjectServiceImplTest {

    private SubjectRepository subjectRepository;
    private SubjectServiceImpl subjectService;

    @BeforeMethod
    public void setUp() {
        subjectRepository = mock(SubjectRepository.class);
        subjectService = new SubjectServiceImpl(subjectRepository, new SubjectDtoEntityMapper());
    }

    @Test
    public void shouldReturnSubjectById() throws Exception {
        Subject subject = new Subject(1L, "ARS", "Automation");
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        SubjectDto result = subjectService.getById(1L);

        assertEquals(result.getId(), Long.valueOf(1L));
        assertEquals(result.getCode(), "ARS");
        assertEquals(result.getName(), "Automation");
    }

    @Test
    public void shouldRejectSubjectWithoutCode() {
        SubjectDto dto = new SubjectDto(null, null, "Automation");

        Exception exception = expectThrows(Exception.class, () -> subjectService.create(dto));

        assertEquals(exception.getMessage(), "Subject code is required.");
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void shouldRejectSubjectWithoutName() {
        SubjectDto dto = new SubjectDto(null, "ARS", null);

        Exception exception = expectThrows(Exception.class, () -> subjectService.create(dto));

        assertEquals(exception.getMessage(), "Subject name is required.");
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void shouldRejectDuplicateSubjectCode() {
        SubjectDto dto = new SubjectDto(null, "ARS", "Automation");
        when(subjectRepository.findByCode("ARS"))
                .thenReturn(Optional.of(new Subject(1L, "ARS", "Existing subject")));

        Exception exception = expectThrows(Exception.class, () -> subjectService.create(dto));

        assertEquals(exception.getMessage(), "Subject code already exists.");
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void shouldDeleteExistingSubject() throws Exception {
        when(subjectRepository.existsById(1L)).thenReturn(true);

        subjectService.delete(1L);

        verify(subjectRepository).deleteById(1L);
    }

    @Test
    public void shouldRejectDeletingMissingSubject() {
        when(subjectRepository.existsById(99L)).thenReturn(false);

        NoSuchElementException exception = expectThrows(
                NoSuchElementException.class,
                () -> subjectService.delete(99L)
        );

        assertEquals(exception.getMessage(), "Subject not found.");
        verify(subjectRepository, never()).deleteById(99L);
    }
}
