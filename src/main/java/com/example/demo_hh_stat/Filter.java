package com.example.demo_hh_stat;

import lombok.*;

@Getter
@Builder()
@AllArgsConstructor
@ToString
public class Filter {
    private String title;
    private String region;
    private String experience;
    private int salary;
    private String requestId;
}
