package com.example.demo_hh_stat;


import com.example.demo_hh_stat.entity.Vacancy;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Bot {
    private Filter filter;
    private final Logic logic;
    private String botToken;

    public Bot(Logic logic, @Value("${bot.token}") String botToken) {
        this.logic = logic;
        this.botToken = botToken;

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

                    List<Vacancy> list = logic.getVacancyFilter(filter);
                    int allResponses = getAllResponses(list);
                    if (list.size() != 0){

                        //show all vacancies
                        /*list.forEach(vacancy -> {
                            bot.execute(new SendMessage(it.message().chat().id(), "Вакансия: " + vacancy.getName() + "\nКоличество откликов: " + vacancy.getCounters().getResponses() + "\nСсылка: http://hh.ru/vacancy/" + vacancy.getId()));
                            System.out.println(vacancy.getId() + " " + vacancy.getName());
                        });*/
                        bot.execute(new SendMessage(it.message().chat().id(), "Количество найденных вакансий: " + list.size() + " и всего откликов " + allResponses + "."));
                    } else {
                        bot.execute(new SendMessage(it.message().chat().id(), "По вашему запросу вакансий не найдено."));
                    }
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

    public int getAllResponses(List<Vacancy> list){
        int totalResponses = 0;
        for (Vacancy v : list) {
            totalResponses += v.getCounters().getResponses();
        }
        return totalResponses;
    }
}
