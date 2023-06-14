package com.example.demo_hh_stat.telegramBot;

import lombok.Data;

@Data
public class UserSession {
    private Long chatId;
    private String title;
    private String region;
    private String experience;
    private Integer salary;
    private ConversationState state;

    public UserSession() {
        this.state = ConversationState.CONVERSATION_STARTED;
    }
}