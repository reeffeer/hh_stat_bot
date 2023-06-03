package com.example.demo_hh_stat;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Bot {
    private Filter filter;
    private final Logic logic;

    //TODO убрать токен в config и вытягивать его в классе-конфигураторе
    private String botToken = "";

    public Bot(Logic logic) {
        this.logic = logic;

        TelegramBot bot = new TelegramBot(botToken);
        bot.setUpdatesListener(element -> {
            System.out.println(element);
            element.forEach(it -> {
                String massive[] = it.message().text().split(" ");
                if (massive[0].equals("/start") || massive.length < 2){
                    bot.execute(new SendMessage(it.message().chat().id(), "Привет! Я покажу тебе количество откликов на интересующую тебя позицию " +
                            "в интересующем тебя городе, а также средний конкурс." +
                            "\nВведи название вакансии(одним словом) и город поиска в виде:" + "\nВакансия Город" + "\nЕсли название города поиска неуникально, введи в формате:" +
                            "\nВакансия Город (Область)"));
                } else {
                    if (massive.length > 2){
                        massive[1] = overRide(massive);
                    }
                    String requestId = String.valueOf(it.message().messageId()).concat(String.valueOf(it.message().from().id()));
                    filter = new Filter.FilterBuilder()
                            .title(massive[0])
                            .region(massive[1])
                            .requestId(requestId)
                            .build();
                    int numVacancies = logic.getNumberOfVacancies(filter);
                    int allResponses = logic.getAllResponses(filter);
                    bot.execute(new SendMessage(it.message().chat().id(),
                            "Найдено вакансий " + numVacancies + " и всего откликов: " + allResponses));
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public String overRide(String s[]){
        String reg = s[1];
        for (int i = 2; i < s.length; i++){
            reg = reg + " " + s[i];
        }
        return reg;
    }
}
