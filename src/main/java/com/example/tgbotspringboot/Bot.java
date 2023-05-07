package com.example.tgbotspringboot;

import com.example.tgbotspringboot.Entity.Filter;
import com.example.tgbotspringboot.Entity.Vacancy;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class Bot {
    private GeneralLogic logic;
    private Filter filter;

    @Autowired
    public Bot(GeneralLogic logic){
        this.logic = logic;
        TelegramBot bot = new TelegramBot("1587428127:AAHk3cxFtUV87O6SR9Fhrj0gn8LVNANcNZ0");
        bot.setUpdatesListener(element -> {
            System.out.println(element);
            element.forEach(it->{

                String[] massive = it.message().text().split(" ");
                if (massive[0].equals("/start")){
                    bot.execute(new SendMessage(it.message().chat().id(), "Приветствую тебя кожанный холоп" +
                            "\nВведи название вакансии(одним словом), город поиска, опыт в виде:" + "\nВакансия Город" + "\nЕсли город поиска не уникальный введи в формате:" +
                            "\nВакансия Город(Область)" + "\nЕсли нужна дополнительная информация, то введи в вормате:" +
                            "\nВакансия Город(Область) Опыт Зарплата"));
                } else {
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

                    List<Vacancy> list = logic.getVacancies(filter);

                    if (list.size() != 0){
                        list.forEach(vacancy -> {
                            bot.execute(new SendMessage(it.message().chat().id(), "Вакансия: " + vacancy.getName() + "\nСсылка: http://hh.ru/vacancy/" + vacancy.getId()));
                            System.out.println(vacancy.getId() + " " + vacancy.getName());
                        });
                    } else {
                        bot.execute(new SendMessage(it.message().chat().id(), "Данных вакансий: " + filter.getNameVacancy() + " в городе(" + filter.getNameRegion() +") не найдено"));
                    }
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


    public String[] overRide(String[] s){
        String[] reg = Arrays.copyOf(s, 5);
        for (int i = s.length; i < 5; i++){
            reg[i] = null;
        }
        reg[3] = "543";
        reg[4] = "id23434534543";

        return reg;
    }
}
