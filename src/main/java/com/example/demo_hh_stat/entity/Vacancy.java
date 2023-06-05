package com.example.demo_hh_stat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "counters")
public class Vacancy {

    private String id;
    private String name;
    private Counters counters;

}
