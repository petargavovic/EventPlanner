/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.service.impl;

import com.events.planner.dto.ReservationDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.Hall;
import com.events.planner.entity.Reservation;
import com.events.planner.entity.ReservationStatus;
import com.events.planner.entity.User;
import com.events.planner.mapper.impl.ReservationDtoEntityMapper;
import com.events.planner.repository.EventRepository;
import com.events.planner.repository.HallRepository;
import com.events.planner.repository.ReservationRepository;
import com.events.planner.repository.UserRepository;
import com.events.planner.service.ReservationService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
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
    public ReservationDto create(ReservationDto dto, String email) throws Exception {
        validateReservation(dto,false);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found."));

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new Exception("Hall not found."));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new Exception("Event not found."));

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setUser(user);
        reservation.setHall(hall);
        reservation.setEvent(event);
        reservation.setTimestamp(LocalDateTime.now());

        reservation.setStatus(ReservationStatus.PENDING);

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
    public Page<ReservationDto> getByStatus(String status, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        ReservationStatus reservationStatus = parseReservationStatus(status);
        return reservationRepository.findByStatus(reservationStatus, pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto, Authentication authentication) throws Exception {
        validateReservation(dto,true);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new Exception("User not found."));

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new Exception("Hall not found."));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new Exception("Event not found."));
        
        boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        
            if (!isAdmin) {

        if (!reservation.getUser().getEmail().equals(email)) {
            throw new Exception("You can only edit your own reservations.");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new Exception("Only PENDING reservations can be edited.");
        }
    }

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            boolean conflict = reservationRepository.existsHallReservationConflict(
                    hall.getId(),
                    dto.getStart(),
                    dto.getEnd(),
                    ReservationStatus.APPROVED,
                    id
            );

            if (conflict) {
                throw new Exception("Hall is already reserved in that time period by an approved reservation.");
            }
        }

        LocalDateTime oldTimestamp = reservation.getTimestamp();
        ReservationStatus oldStatus = reservation.getStatus();

        reservationMapper.updateEntity(reservation, dto);

        reservation.setTimestamp(oldTimestamp);
        reservation.setStatus(oldStatus);
        reservation.setUser(user);
        reservation.setHall(hall);
        reservation.setEvent(event);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public ReservationDto updateStatus(Long id, String status) throws Exception {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found."));
            
        ReservationStatus newStatus = parseReservationStatus(status);

        if (newStatus == ReservationStatus.APPROVED) {
            boolean conflict = reservationRepository.existsHallReservationConflict(
                    reservation.getHall().getId(),
                    reservation.getStart(),
                    reservation.getEnd(),
                    ReservationStatus.APPROVED,
                    reservation.getId()
            );

            if (conflict) {
                throw new Exception("Cannot approve reservation. Hall is already reserved in that time period.");
            }
        }
        else if(newStatus == ReservationStatus.CANCELLED)
            System.out.println("Cancelled");

        reservation.setStatus(newStatus);

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

    private void validateReservation(ReservationDto dto, boolean requireUserId) throws Exception {
        if (dto.getStart() == null) {
            throw new Exception("Start time is required.");
        }
        if (dto.getEnd() == null) {
            throw new Exception("End time is required.");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new Exception("End time must be after start time.");
        }
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
    if (dto.getStart().toLocalTime().isBefore(openingTime)) {
        throw new Exception("Reservations cannot start before 08:00.");
    }
    if (dto.getEnd().toLocalTime().isAfter(closingTime)) {
        throw new Exception("Reservations must end by 20:00.");
    }
        if (requireUserId && dto.getUserId() == null) {
            throw new Exception("User ID is required.");
        }
        if (dto.getHallId() == null) {
            throw new Exception("Hall ID is required.");
        }
        if (dto.getEventId() == null) {
            throw new Exception("Event ID is required.");
        }
    }

    @Override
    public ReservationDto cancelMyReservation(Long id, String email) throws Exception {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new Exception("You can cancel only your own reservation.");
        }
            
        reservation.setStatus(ReservationStatus.CANCELLED);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public ReservationDto updateMyReservation(Long id, ReservationDto dto, String email) throws Exception {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new Exception("You can update only your own reservation.");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new Exception(reservation.getStatus() + " reservation cannot be updated.");
        }

        dto.setUserId(reservation.getUser().getId());

        Authentication auth = null;
        
        return update(id, dto, auth);
    }
    
    @Override
    public Page<ReservationDto> getFiltered(int page, int size, String status, Long userId, Long hallId, Long eventId, 
            String sortBy, String sortDir) {
        switch (sortBy) {
    case "user":
        sortBy = "user.name";
        break;
    case "hall":
        sortBy = "hall.name";
        break;
    case "event":
        sortBy = "event.name";
        break;
    case "created":
        sortBy = "timestamp";
        break;
}

Sort sort = sortDir.equalsIgnoreCase("desc")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

Pageable pageable = PageRequest.of(page, size, sort);

    Specification<Reservation> spec = (root, query, cb) -> cb.conjunction();

    if (status != null && !status.isBlank()) {
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), ReservationStatus.valueOf(status)));
    }

    if (userId != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId));
    }

    if (hallId != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("hall").get("id"), hallId));
    }

    if (eventId != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("event").get("id"), eventId));
    }

    return reservationRepository.findAll(spec, pageable)
            .map(reservationMapper::toDto);
    }

    private ReservationStatus parseReservationStatus(String status) throws Exception {
        if (status == null || status.isBlank()) {
            throw new Exception("Reservation status is required.");
        }

        try {
            return ReservationStatus.valueOf(status.trim().toUpperCase());
        } catch (Exception e) {
            throw new Exception("Invalid reservation status.");
        }
    }

}
