/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.service.impl;

import com.events.planner.dto.ReservationDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.Hall;
import com.events.planner.entity.Reservation;
import com.events.planner.entity.User;
import com.events.planner.mapper.impl.ReservationDtoEntityMapper;
import com.events.planner.repository.EventRepository;
import com.events.planner.repository.HallRepository;
import com.events.planner.repository.ReservationRepository;
import com.events.planner.repository.UserRepository;
import com.events.planner.service.ReservationService;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author MAU
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;
    private final ReservationDtoEntityMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
            UserRepository userRepository,
            HallRepository hallRepository,
            EventRepository eventRepository,
            ReservationDtoEntityMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDto create(ReservationDto dto) throws Exception {
        validateReservation(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new Exception("User not found."));

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new Exception("Hall not found."));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new Exception("Event not found."));

        boolean conflict = reservationRepository.existsHallReservationConflict(
                hall.getId(),
                dto.getStart(),
                dto.getEnd(),
                null
        );

        if (conflict) {
            throw new Exception("Hall is already reserved in that time period.");
        }

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setUser(user);
        reservation.setHall(hall);
        reservation.setEvent(event);
        reservation.setTimestamp(LocalDateTime.now());

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public ReservationDto getById(Long id) throws Exception {
        return reservationRepository.findById(id)
                .map(reservationMapper::toDto)
                .orElseThrow(() -> new Exception("Reservation not found."));
    }

    @Override
    public Page<ReservationDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return reservationRepository.findAll(pageable).map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> getByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return reservationRepository.findByUserId(userId, pageable).map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> getByHallId(Long hallId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return reservationRepository.findByHallId(hallId, pageable).map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> getByEventId(Long eventId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return reservationRepository.findByEventId(eventId, pageable).map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> getByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return reservationRepository.findByStatusContainingIgnoreCase(status, pageable).map(reservationMapper::toDto);
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto) throws Exception {
        validateReservation(dto);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new Exception("User not found."));

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new Exception("Hall not found."));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new Exception("Event not found."));

        boolean conflict = reservationRepository.existsHallReservationConflict(
                hall.getId(),
                dto.getStart(),
                dto.getEnd(),
                id
        );

        if (conflict) {
            throw new Exception("Hall is already reserved in that time period.");
        }

        LocalDateTime oldTimestamp = reservation.getTimestamp();

        reservationMapper.updateEntity(reservation, dto);
        reservation.setTimestamp(oldTimestamp);
        reservation.setUser(user);
        reservation.setHall(hall);
        reservation.setEvent(event);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (!reservationRepository.existsById(id)) {
            throw new Exception("Reservation not found.");
        }
        reservationRepository.deleteById(id);
    }

    private void validateReservation(ReservationDto dto) throws Exception {
        if (dto.getStart() == null) {
            throw new Exception("Start time is required.");
        }
        if (dto.getEnd() == null) {
            throw new Exception("End time is required.");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new Exception("End time must be after start time.");
        }
        if (dto.getUserId() == null) {
            throw new Exception("User ID is required.");
        }
        if (dto.getHallId() == null) {
            throw new Exception("Hall ID is required.");
        }
        if (dto.getEventId() == null) {
            throw new Exception("Event ID is required.");
        }
    }
}
