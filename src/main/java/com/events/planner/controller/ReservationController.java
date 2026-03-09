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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author MAU
 */

    
@Tag(name = "Reservations", description = "CRUD operations for reservations")
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
    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto dto) throws Exception {
        return new ResponseEntity<>(reservationService.create(dto), HttpStatus.CREATED);
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

    @Operation(summary = "Get all reservations (paged)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<Page<ReservationDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getAll(page, size));
    }

    @Operation(summary = "Get reservations by user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationDto>> getByUserId(
            @Parameter(description = "User id", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getByUserId(userId, page, size));
    }

    @Operation(summary = "Get reservations by hall id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/hall/{hallId}")
    public ResponseEntity<Page<ReservationDto>> getByHallId(
            @Parameter(description = "Hall id", example = "1")
            @PathVariable Long hallId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getByHallId(hallId, page, size));
    }

    @Operation(summary = "Get reservations by event id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<ReservationDto>> getByEventId(
            @Parameter(description = "Event id", example = "1")
            @PathVariable Long eventId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getByEventId(eventId, page, size));
    }

    @Operation(summary = "Get reservations by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/status")
    public ResponseEntity<Page<ReservationDto>> getByStatus(
            @Parameter(description = "Reservation status", example = "APPROVED")
            @RequestParam String status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reservationService.getByStatus(status, page, size));
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
            @RequestBody ReservationDto dto) throws Exception {
        return ResponseEntity.ok(reservationService.update(id, dto));
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

