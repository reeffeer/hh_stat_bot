package com.example.demo_hh_stat.entity;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "counters")
public class Vacancy {

    private String id;
    private String name;
    private Counters counters;

}
