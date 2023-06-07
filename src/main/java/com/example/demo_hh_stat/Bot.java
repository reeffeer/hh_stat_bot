package com.example.demo_hh_stat;


import com.example.demo_hh_stat.entity.Vacancy;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Bot {
    private final Logic logic;
    private String botToken;
    private enum BotState {
        START, TITLE, CITY, EXPERIENCE, SALARY
    }
    private BotState currentState = BotState.START;
    private Filter filter;

    public Bot(Logic logic, @Value("${bot.token}") String botToken) {
        this.logic = logic;
        this.botToken = botToken;
        //start(logic, botToken);
        start();
    }

    public void start() {
        TelegramBot bot = new TelegramBot(botToken);
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update) {
        if (update.message() != null) {
            long chatId = update.message().chat().id();
            String messageText = update.message().text();

            switch (currentState) {
                case START:
                    handleStartCommand(chatId);
                    break;
                case TITLE:
                    handleTitleInput(chatId, messageText);
                    break;
                case CITY:
                    handleCityInput(chatId, messageText);
                    break;
                case EXPERIENCE:
                    handleExperienceInput(chatId, messageText);
                    break;
                case SALARY:
                    handleSalaryInput(chatId, messageText);
                    break;
            }
        }
    }

    private void handleStartCommand(long chatId) {
        sendMessage(chatId, "Привет! Я покажу вам количество откликов на интересующую вас позицию " +
                "в интересующем вас городе, а также средний конкурс.\n" +
                "Введите название вакансии: ");
        currentState = BotState.TITLE;
        filter = new Filter();
    }

    private void handleTitleInput(long chatId, String title) {
        filter.setTitle(title);
        sendMessage(chatId, "Введите название города:");
        currentState = BotState.CITY;
    }

    private void handleCityInput(long chatId, String city) {
        filter.setRegion(city);
        sendMessage(chatId, "Введите опыт работы (если не указывать, введите 0):");
        currentState = BotState.EXPERIENCE;
    }

    private void handleExperienceInput(long chatId, String experience) {
        if (!experience.equals("0")) {
            filter.setExperience(experience);
        }
        sendMessage(chatId, "Введите желаемую зарплату (если не указывать, введите 0):");
        currentState = BotState.SALARY;
    }

    private void handleSalaryInput(long chatId, String salary) {
        if (!salary.equals("0")) {
            filter.setSalary(Integer.parseInt(salary));
        }

        List<Vacancy> vacancies = logic.getVacancyFilter(filter);
        int numVacancies = vacancies.size();
        int allResponses = logic.getAllResponses(vacancies);

        if (numVacancies != 0) {
            sendMessage(chatId, "Найдено вакансий: " + numVacancies + " и всего откликов: " + allResponses);
        } else {
            sendMessage(chatId, "По вашему запросу вакансий не найдено.");
        }

        currentState = BotState.START;
        filter = null;
    }

    private void sendMessage(long chatId, String message) {
        TelegramBot bot = new TelegramBot(botToken);
        SendResponse response = bot.execute(new SendMessage(chatId, message));
        System.out.println(response.isOk());
    }
}
