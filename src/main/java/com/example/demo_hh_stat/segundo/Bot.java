package com.example.demo_hh_stat.segundo;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Bot {
Filter filter;
    private Logic logic;

    @Autowired
    public Bot(Logic logic){
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
                    //List<Vacancy> list = hhApi.getVacanciesFilterNameRegion(massive[0],massive[1]);
                    String requestId = String.valueOf(it.message().messageId()).concat(String.valueOf(it.message().from().id()));
                    filter = new Filter.FilterBuilder(massive[0], massive[1], requestId).build();
                    int numVacancies = logic.getNumberOfVacancies(filter);
                    bot.execute(new SendMessage(it.message().chat().id(),
                            "Найдено вакансий " + numVacancies));
                    /*if (list.size() != 0){
                        list.forEach(vacancy -> {
                            bot.execute(new SendMessage(it.message().chat().id(), "Вакансия: " + vacancy.getName() + "\nСсылка: http://hh.ru/vacancy/" + vacancy.getId()));
                            System.out.println(vacancy.getId() + " " + vacancy.getName());
                        });
                    } else {
                        bot.execute(new SendMessage(it.message().chat().id(), "Данных вакансий в городе " + massive[1] +" не найдено"));
                    }*/
                }

                /*else {
                    String requestId = String.valueOf(it.message().messageId()).concat(String.valueOf(it.message().from().id()));
                    switch (massive.length) {
                        case 1:
                            filter = new Filter.FilterBuilder(massive[0], "не указан", requestId).build();
                            break;
                        case 2:
                            filter = new Filter.FilterBuilder(massive[0], massive[1], requestId).build();
                            break;
                        case 3:
                            filter = new Filter.FilterBuilder(massive[0], massive[1], requestId)
                                    .experience(massive[1])
                                    .build();
                            break;
                        case 4:
                            int i;
                            try {
                                i = Integer.parseInt(massive[3]);
                            } catch (NumberFormatException e) {
                                i = -1;
                            }
                            filter = new Filter.FilterBuilder(massive[0], massive[1], requestId)
                                    .experience(massive[2])
                                    .salary(i)
                                    .build();
                            break;
                        default: filter = new Filter.FilterBuilder("Java", "Москва", requestId).build();
                    }

                    int numberOfVacancies = logic.getNumberOfVacancies(filter);

                    *//*if (list.size() != 0){
                        list.forEach(vacancy -> {
                            bot.execute(new SendMessage(it.message().chat().id(), "Вакансия: " + vacancy.getName() + "\nСсылка: http://hh.ru/vacancy/" + vacancy.getId()));
                            System.out.println(vacancy.getId() + " " + vacancy.getName());
                        });
                    } else {
                        bot.execute(new SendMessage(it.message().chat().id(), "Данных вакансий: " + filter.getNameVacancy() + " в городе(" + filter.getNameRegion() +") не найдено"));
                    }*//*
                }*/
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
