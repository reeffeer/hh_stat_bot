package org.example.model;

import lombok.Data;

@Data
public class UserRequest {
    private Long chatId;
    private String vacation;
    private String state;
    private String city;
    private String experience;
}
