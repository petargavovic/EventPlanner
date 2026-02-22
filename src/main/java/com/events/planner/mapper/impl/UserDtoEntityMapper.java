/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.mapper.impl;

import com.events.planner.dto.UserDto;
import com.events.planner.entity.User;
import com.events.planner.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;


@Component
public class UserDtoEntityMapper implements DtoEntityMapper<UserDto, User> {

    @Override
    public UserDto toDto(User e) {
        return new UserDto(e.getId(), e.getName(), e.getSurname(),
                e.getEmail(), e.getPassword(), e.isAdmin());
    }

    @Override
    public User toEntity(UserDto t) {
        return new User(t.getId(), t.getName(), t.getSurname(),
                t.getEmail(), t.getPassword(), t.isAdmin());
    }
    
        public void updateEntity(User entity, UserDto dto) {
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setAdmin(dto.isAdmin());
    }
}