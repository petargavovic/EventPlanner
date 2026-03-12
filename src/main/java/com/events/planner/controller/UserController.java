package com.events.planner.controller;

import com.events.planner.dto.UserDto;
import com.events.planner.service.UserService;
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

@Tag(name = "Users", description = "CRUD operations for users + login")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    @Operation(summary = "Create user", description = "Creates a new user. Email must be unique.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) throws Exception {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(Authentication authentication) throws Exception {
        return ResponseEntity.ok(userService.getByEmail(authentication.getName()));
    }

    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(
            Authentication authentication,
            @RequestBody UserDto dto) throws Exception {
        return ResponseEntity.ok(userService.updateByEmail(authentication.getName(), dto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto dto) throws Exception {
        dto.setAdmin(false);
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get user by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "User not found",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(
            @Parameter(description = "User id", example = "1")
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getById(id));
    }

    
    @Operation(summary = "Get all users (paged)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK")
    })
    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }


    @Operation(summary = "Update user", description = "Updates an existing user by id.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "User not found / email already exists",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @Parameter(description = "User id", example = "1")
            @PathVariable Long id,
            @RequestBody UserDto dto) throws Exception {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Delete user")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted"),
        @ApiResponse(responseCode = "400", description = "User not found",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "User id", example = "1")
            @PathVariable Long id) throws Exception {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Login", description = "Returns user if email/password are correct.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto dto) throws Exception {
        return ResponseEntity.ok(userService.login(dto.getEmail(), dto.getPassword()));
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
