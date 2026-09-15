package com.events.planner.service.impl;

import com.events.planner.dto.HallDto;
import com.events.planner.entity.Hall;
import com.events.planner.entity.HallType;
import com.events.planner.mapper.impl.HallDtoEntityMapper;
import com.events.planner.repository.HallRepository;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

public class HallServiceImplTest {

    private HallRepository hallRepository;
    private HallServiceImpl hallService;

    @BeforeMethod
    public void setUp() {
        hallRepository = mock(HallRepository.class);
        hallService = new HallServiceImpl(hallRepository, new HallDtoEntityMapper());
    }

    @Test
    public void shouldCreateHall() throws Exception {
        HallDto dto = new HallDto(null, "Room 101", 80, "First floor", "CLASSROOM", "Projector");

        when(hallRepository.save(any(Hall.class))).thenAnswer(invocation -> {
            Hall hall = invocation.getArgument(0);
            hall.setId(1L);
            return hall;
        });

        HallDto result = hallService.create(dto);

        ArgumentCaptor<Hall> captor = ArgumentCaptor.forClass(Hall.class);
        verify(hallRepository).save(captor.capture());

        assertEquals(result.getId(), Long.valueOf(1L));
        assertEquals(result.getName(), "Room 101");
        assertEquals(captor.getValue().getType(), HallType.CLASSROOM);
        assertEquals(captor.getValue().getCapacity(), 80);
    }

    @Test
    public void shouldReturnHallById() throws Exception {
        Hall hall = new Hall(1L, "Room 101", 80, "First floor", HallType.CLASSROOM, "Projector");
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));

        HallDto result = hallService.getById(1L);

        assertEquals(result.getId(), Long.valueOf(1L));
        assertEquals(result.getName(), "Room 101");
    }

    @Test
    public void shouldUpdateHall() throws Exception {
        Hall existing = new Hall(1L, "Old room", 40, "Ground floor", HallType.CLASSROOM, "Board");
        HallDto update = new HallDto(1L, "Updated room", 100, "Second floor", "AMPHITHEATRE", "Projector");

        when(hallRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(hallRepository.save(any(Hall.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HallDto result = hallService.update(1L, update);

        assertEquals(result.getName(), "Updated room");
        assertEquals(result.getCapacity(), 100);
        assertEquals(result.getType(), "AMPHITHEATRE");
        verify(hallRepository).save(existing);
    }

    @DataProvider(name = "invalidHallRequiredFields")
    public Object[][] invalidHallRequiredFields() {
        return new Object[][]{
            {null, "CLASSROOM", "Hall name is required."},
            {"   ", "CLASSROOM", "Hall name is required."},
            {"Room 101", null, "Hall type is required."},
            {"Room 101", "   ", "Hall type is required."}
        };
    }

    @Test(dataProvider = "invalidHallRequiredFields")
    public void shouldRejectHallWithMissingRequiredField(
            String name,
            String type,
            String expectedMessage) {

        HallDto dto = new HallDto(
                null,
                name,
                80,
                "First floor",
                type,
                null
        );

        Exception exception = expectThrows(
                Exception.class,
                () -> hallService.create(dto)
        );

        assertEquals(exception.getMessage(), expectedMessage);
        verify(hallRepository, never()).save(any(Hall.class));
    }

    @Test
    public void shouldRejectHallWithNegativeCapacity() {
        HallDto dto = new HallDto(null, "Room 101", -1, "First floor", "CLASSROOM", null);

        Exception exception = expectThrows(Exception.class, () -> hallService.create(dto));

        assertEquals(exception.getMessage(), "Capacity cannot be negative.");
        verify(hallRepository, never()).save(any(Hall.class));
    }
}
