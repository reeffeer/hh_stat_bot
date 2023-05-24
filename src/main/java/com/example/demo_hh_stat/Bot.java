package com.example.demo_hh_stat;


import com.example.demo_hh_stat.entity.Filter;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Bot {
Filter filter;
    private Logic logic;

    public Bot(Logic logic) {
        this.logic = logic;
        TelegramBot bot = new TelegramBot("5969411582:AAFB5FwPZp-SCKP7owsqb8eU335xjcr94TM");
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
                    filter = new Filter.FilterBuilder(massive[0], massive[1], requestId).build();
                    int numVacancies = logic.getNumberOfVacancies(filter);
                    int allResponses = logic.getNumberOfResponses(filter);
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
