package com.example.demo_hh_stat.ejemplo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class Filter {
    String title;
    int cityId;
    int pageNumber;
}
