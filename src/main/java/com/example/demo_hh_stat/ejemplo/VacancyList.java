package com.example.demo_hh_stat.ejemplo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VacancyList {
    List<Vacancy> vacancies = new ArrayList<>();
}
