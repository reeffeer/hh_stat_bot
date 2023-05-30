package com.example.demo_hh_stat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Vacancy {

    private String id;
    private String name;
    private Counters counters;

//    @Override
//    public String toString() {
//        return "id = " + id +
//                " -> name = " + name;
//    }
}
