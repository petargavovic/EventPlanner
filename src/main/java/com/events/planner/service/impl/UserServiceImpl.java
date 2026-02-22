package com.events.planner.service.impl;

import com.events.planner.dto.UserDto;
import com.events.planner.entity.User;
import com.events.planner.mapper.impl.UserDtoEntityMapper;
import com.events.planner.repository.UserRepository;
import com.events.planner.service.UserService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoEntityMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserDtoEntityMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto create(UserDto dto) throws Exception {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new Exception("Email is required.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new Exception("Password is required.");
        }

        Optional<User> existing = userRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new Exception("Email already exists.");
        }

        User saved = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(saved);
    }

    @Override
    public UserDto getById(Long id) throws Exception {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new Exception("User not found."));
    }

    @Override
    public Page<UserDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public UserDto getByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new Exception("User not found."));
    }

    @Override
    public Page<UserDto> getByAdmin(boolean admin, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return userRepository.findByAdmin(admin, pageable).map(userMapper::toDto);
    }

    @Override
    public UserDto update(Long id, UserDto dto) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found."));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            Optional<User> existing = userRepository.findByEmail(dto.getEmail());
            if (existing.isPresent()) {
                throw new Exception("Email already exists.");
            }
        }

        userMapper.updateEntity(user, dto);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new Exception("User not found.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto login(String email, String password) throws Exception {
        return userRepository.findByEmailAndPassword(email, password)
                .map(userMapper::toDto)
                .orElseThrow(() -> new Exception("Invalid user!"));
    }
}