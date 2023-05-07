package com.example.tgbotspringboot.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    private String id;
    private String name;
    private int responses;
}
