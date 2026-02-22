package com.events.planner.dto;

public class HallDto {

    private Long id;
    private String name;
    private int capacity;
    private String location;
    private String type;
    private String equipment;

    public HallDto() {
    }

    public HallDto(Long id, String name, int capacity, String location, String type, String equipment) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.type = type;
        this.equipment = equipment;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "HallDto{" + "id=" + id + ", name=" + name + ", capacity=" + capacity
                + ", location=" + location + ", type=" + type + ", equipment=" + equipment + '}';
    }
}