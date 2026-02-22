package com.events.planner.dto;

public class EventDto {

    private Long id;
    private String name;
    private String type;
    private String description;
    private int capacity;

    private Long subjectId;

    public EventDto() {
    }

    public EventDto(Long id, String name, String type, String description, int capacity, Long subjectId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.capacity = capacity;
        this.subjectId = subjectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "EventDto{" + "id=" + id + ", name=" + name + ", type=" + type
                + ", description=" + description + ", capacity=" + capacity
                + ", subjectId=" + subjectId + '}';
    }
}