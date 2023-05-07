package com.example.tgbotspringboot.Entity;

import java.util.List;

public class Country {
    private String name;
    private List<Region> areas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getAreas() {
        return areas;
    }

    public void setAreas(List<Region> areas) {
        this.areas = areas;
    }
}
