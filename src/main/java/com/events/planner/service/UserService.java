package com.events.planner.service;

import com.events.planner.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto create(UserDto dto) throws Exception;

    UserDto getById(Long id) throws Exception;
    Page<UserDto> getAll(int page, int size);

    UserDto getByEmail(String email) throws Exception;
    Page<UserDto> getByAdmin(boolean admin, int page, int size);

    UserDto update(Long id, UserDto dto) throws Exception;

    void delete(Long id) throws Exception;

    UserDto login(String email, String password) throws Exception;
}