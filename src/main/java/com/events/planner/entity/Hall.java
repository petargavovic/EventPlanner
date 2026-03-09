package com.events.planner.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HallType type;

    @Column(name = "equipment")
    private String equipment;

    @OneToMany(mappedBy = "hall")
    private List<Reservation> reservations;

    public Hall() {
    }

    public Hall(Long id, String name, int capacity, String location, HallType type, String equipment) {
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

    public HallType getType() {
        return type;
    }

    public void setType(HallType type) {
        this.type = type;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Hall{" + "id=" + id + ", name=" + name + ", capacity=" + capacity
                + ", location=" + location + ", type=" + type + ", equipment=" + equipment + '}';
    }
}
