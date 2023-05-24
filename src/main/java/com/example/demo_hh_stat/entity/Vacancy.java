package com.example.demo_hh_stat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Vacancy {

    //нужно еще поле int responses для работы метода получения всех откликов по всем вакансиям
    private String id;
    private String name;
    private Counters counters;

//    private int responses;

//    @Override
//    public String toString() {
//        return "id = " + id +
//                " -> name = " + name;
//    }
}
