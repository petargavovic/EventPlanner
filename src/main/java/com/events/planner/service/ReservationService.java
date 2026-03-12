/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.service;

import com.events.planner.dto.ReservationDto;
import org.springframework.data.domain.Page;

/**
 *
 * @author MAU
 */
public interface ReservationService {

    ReservationDto create(ReservationDto dto, String email) throws Exception;

    ReservationDto getById(Long id) throws Exception;

    Page<ReservationDto> getAll(int page, int size);

    Page<ReservationDto> getByUserId(Long userId, int page, int size);

    Page<ReservationDto> getByHallId(Long hallId, int page, int size);

    Page<ReservationDto> getByEventId(Long eventId, int page, int size);

    ReservationDto update(Long id, ReservationDto dto) throws Exception;

    ReservationDto updateStatus(Long id, String status) throws Exception;

    ReservationDto cancelMyReservation(Long id, String email) throws Exception;

    ReservationDto updateMyReservation(Long id, ReservationDto dto, String email) throws Exception;

    Page<ReservationDto> getByStatus(String status, int page, int size) throws Exception;

    void delete(Long id) throws Exception;
}
