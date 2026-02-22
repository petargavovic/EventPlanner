package com.events.planner.dto;

public class SubjectDto {

    private Long id;
    private String code;
    private String name;

    public SubjectDto() {
    }

    public SubjectDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubjectDto{" + "id=" + id + ", code=" + code + ", name=" + name + '}';
    }
}