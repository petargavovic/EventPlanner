/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.controller;
import com.events.planner.dto.SubjectDto;
import com.events.planner.service.SubjectService;
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
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author MAU
 */
@Tag(name = "Subjects", description = "CRUD operations for subjects")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Create subject", description = "Creates a new subject. Code must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error / code already exists",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SubjectDto> create(@RequestBody SubjectDto dto) throws Exception {
        return new ResponseEntity<>(subjectService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get subject by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Subject not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getById(
            @Parameter(description = "Subject id", example = "1")
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(subjectService.getById(id));
    }

    @Operation(summary = "Get all subjects (paged)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<Page<SubjectDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subjectService.getAll(page, size));
    }

    @Operation(summary = "Get subject by code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Subject not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<SubjectDto> getByCode(
            @Parameter(description = "Subject code", example = "CS101")
            @PathVariable String code) throws Exception {
        return ResponseEntity.ok(subjectService.getByCode(code));
    }

    @Operation(summary = "Search subjects by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<SubjectDto>> searchByName(
            @Parameter(description = "Subject name", example = "Programming")
            @RequestParam String name,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subjectService.searchByName(name, page, size));
    }

    @Operation(summary = "Update subject", description = "Updates an existing subject by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Subject not found / code already exists",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> update(
            @Parameter(description = "Subject id", example = "1")
            @PathVariable Long id,
            @RequestBody SubjectDto dto) throws Exception {
        return ResponseEntity.ok(subjectService.update(id, dto));
    }

    @Operation(summary = "Delete subject")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "400", description = "Subject not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Subject id", example = "1")
            @PathVariable Long id) throws Exception {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
