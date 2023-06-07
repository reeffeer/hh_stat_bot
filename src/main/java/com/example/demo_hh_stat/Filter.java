package com.example.demo_hh_stat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder()
@AllArgsConstructor
@ToString
public class Filter {
    private String title;
    private String region;
    private String experience;
    private Integer salary;
    private String requestId;
}