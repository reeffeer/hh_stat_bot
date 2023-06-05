package com.example.demo_hh_stat.entity;

import lombok.Data;

import java.util.List;

@Data
public class Region {
    private String name;
    private List<City> areas;
}
