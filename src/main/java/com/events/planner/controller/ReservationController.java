/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.controller;

import com.events.planner.dto.ReservationDto;
import com.events.planner.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author MAU
 */
@Tag(name = "Reservations", description = "CRUD operations for reservations")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create reservation", description = "Creates a new reservation if there is no time conflict.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(schema = @Schema(implementation = ReservationDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error / hall conflict / related entity not found",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto dto, Authentication authentication) throws Exception {
        return new ResponseEntity<>(
                reservationService.create(dto, authentication.getName()),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get reservation by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = ReservationDto.class))),
        @ApiResponse(responseCode = "400", description = "Reservation not found",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getById(
            @Parameter(description = "Reservation id", example = "1")
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @Operation(summary = "Get all reservations (paged) with filters")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK")
    })
@GetMapping
public ResponseEntity<Page<ReservationDto>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,

        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long hallId,
        @RequestParam(required = false) Long eventId,

        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir
) {
    return ResponseEntity.ok(
            reservationService.getFiltered(
                    page,
                    size,
                    status,
                    userId,
                    hallId,
                    eventId,
                    sortBy,
                    sortDir
            )
    );
}


    @Operation(summary = "Update reservation", description = "Updates an existing reservation by id.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = ReservationDto.class))),
        @ApiResponse(responseCode = "400", description = "Reservation not found / validation error / hall conflict",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> update(
            @Parameter(description = "Reservation id", example = "1")
            @PathVariable Long id,
            @RequestBody ReservationDto dto,
            Authentication authentication) throws Exception {
        return ResponseEntity.ok(
                reservationService.update(id, dto, authentication)
        );
    }

    @Operation(summary = "Delete reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted"),
        @ApiResponse(responseCode = "400", description = "Reservation not found",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Reservation id", example = "1")
            @PathVariable Long id) throws Exception {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) throws Exception {
        return ResponseEntity.ok(reservationService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelMyReservation(
            @PathVariable Long id,
            Authentication authentication) throws Exception {
        return ResponseEntity.ok(
                reservationService.cancelMyReservation(id, authentication.getName())
        );
    }

    @PutMapping("/{id}/me")
    public ResponseEntity<ReservationDto> updateMyReservation(
            @PathVariable Long id,
            @RequestBody ReservationDto dto,
            Authentication authentication) throws Exception {
        return ResponseEntity.ok(
                reservationService.updateMyReservation(id, dto, authentication.getName())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
