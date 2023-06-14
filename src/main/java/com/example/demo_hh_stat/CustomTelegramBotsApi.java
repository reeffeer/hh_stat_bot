package com.example.demo_hh_stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import com.example.demo_hh_stat.telegramBot.HhBot;

@Component
public class CustomTelegramBotsApi extends TelegramBotsApi {

    public CustomTelegramBotsApi(Class<? extends BotSession> botSessionClass, HhBot hhBot) throws TelegramApiException {
        super(botSessionClass);
        this.registerBot(hhBot);
    }
}
