/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.controller;

import com.events.planner.dto.EventDto;
import com.events.planner.service.EventService;
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

@Tag(name = "Events", description = "CRUD operations for events")
@RestController
@RequestMapping("/api/events")
public class EventController {
        private final EventService eventService;
  public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Create event", description = "Creates a new event.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<EventDto> create(@RequestBody EventDto dto) throws Exception {
        return new ResponseEntity<>(eventService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get event by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getById(
            @Parameter(description = "Event id", example = "1")
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @Operation(summary = "Get all events (paged)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<Page<EventDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getAll(page, size));
    }

    @Operation(summary = "Get events by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid event type",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/type")
    public ResponseEntity<Page<EventDto>> getByType(
            @Parameter(description = "Event type", example = "LECTURE")
            @RequestParam String type,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) throws Exception {
        return ResponseEntity.ok(eventService.getByType(type, page, size));
    }

    @Operation(summary = "Search events by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<EventDto>> searchByName(
            @Parameter(description = "Event name", example = "Java")
            @RequestParam String name,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.searchByName(name, page, size));
    }

    @Operation(summary = "Get events by subject id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<Page<EventDto>> getBySubjectId(
            @Parameter(description = "Subject id", example = "1")
            @PathVariable Long subjectId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getBySubjectId(subjectId, page, size));
    }

    @Operation(summary = "Update event", description = "Updates an existing event by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Event not found / validation error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(
            @Parameter(description = "Event id", example = "1")
            @PathVariable Long id,
            @RequestBody EventDto dto) throws Exception {
        return ResponseEntity.ok(eventService.update(id, dto));
    }

    @Operation(summary = "Delete event")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "400", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Event id", example = "1")
            @PathVariable Long id) throws Exception {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
