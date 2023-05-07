package com.example.tgbotspringboot.Entity;

import java.util.List;

public class Region {
    private String name;
    private List<Town> areas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Town> getAreas() {
        return areas;
    }

    public void setAreas(List<Town> areas) {
        this.areas = areas;
    }
}
