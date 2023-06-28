package com.example.demo_hh_stat.telegramBot;

import com.example.demo_hh_stat.Logic;
import com.example.demo_hh_stat.entity.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.example.demo_hh_stat.telegramBot.services.UserSessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HhBot extends TelegramLongPollingBot {
    private final Logic logic;
    private List<Vacancy> vacancies = new ArrayList<>();
    @Value("${bot.token}")
    private String botToken;
    UserSessionService userSessionService = new UserSessionService();
    private final HashMap<String, String> experienceMap = new HashMap<>() {{
        put("Нет опыта", "noExperience");
        put("Опыт от 1 до 3 лет", "between1And3");
        put("Опыт от 3 до 6 лет", "between3And6");
        put("Опыт больше 6 лет", "moreThan6");
    }};

    @Override
    public String getBotUsername() {
        return "hh_stat_study_bot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            UserSession userSession = userSessionService.getSession(chatId);
            userSession.setChatId(chatId);
            ConversationState state = userSession.getState();

            if (messageText.equals("/start")) {
                userSession.setState(ConversationState.CONVERSATION_STARTED);
                sendInitialKeyboard(chatId);
            } else if (messageText.equals("Найти вакансии")) {
                userSession.setState(ConversationState.SET_PARAMETERS);
                sendNestedKeyboard(chatId);
            } else if (messageText.equals("Город")){
                userSession.setState(ConversationState.SET_AREA_NAME);
                sendMessage(chatId, "Введите название города:");
            } else if (state == ConversationState.SET_AREA_NAME){
                userSession.setRegion(messageText);
                userSession.setState(ConversationState.SET_PARAMETERS);
            } else if (messageText.equals("Название вакансии")){
                userSession.setState(ConversationState.SET_VACATION_NAME);
                sendMessage(chatId, "Введите название вакансии:");
            } else if (state == ConversationState.SET_VACATION_NAME){
                userSession.setTitle(messageText);
                userSession.setState(ConversationState.SET_PARAMETERS);
            } else if (messageText.equals("Опыт")){
                userSession.setState(ConversationState.SET_EXPERIENCE);
                sendExperienceKeyboard(chatId);
            } else if (messageText.equals("Зарплата")){
                userSession.setState(ConversationState.SET_SALARY);
                sendMessage(chatId, "Введите зарплату:");
            } else if (state == ConversationState.SET_EXPERIENCE){
                String experienceId = experienceMap.get(messageText);
                if (experienceId != null){
                    userSession.setExperience(experienceId);
                    userSession.setState(ConversationState.SET_PARAMETERS);
                    sendNestedKeyboard(chatId);
                } else {
                    sendMessage(chatId, "Такого опыта не найдено. Выберите из предлагаемых вариантов:");
                    sendExperienceKeyboard(chatId);
                }
            } else if (state == ConversationState.SET_SALARY){
                if (messageText.matches("-?\\d+")) {
                    userSession.setSalary(Integer.parseInt(messageText));
                    userSession.setState(ConversationState.SET_PARAMETERS);
                    sendNestedKeyboard(chatId);
                } else {
                    sendMessage(chatId, "Вы ввели некоректное число. Попробуйте снова: ");
                }

            } else if (messageText.equals("Найти")) {
                if (userSession.getTitle() == null && userSession.getRegion() != null) {
                    sendMessage(chatId, "Вы не ввели название вакансии.");
                }
                else if (userSession.getTitle() != null && userSession.getRegion() == null) {
                    sendMessage(chatId, "Вы не ввели название региона");
                }
                else if (userSession.getTitle() == null && userSession.getRegion() == null) {
                    sendMessage(chatId, "Вы не ввели название региона и имя вакансии.");
                }
                else {
                    userSession.setState(ConversationState.SHOW_VACATIONS);

                    vacancies = logic.getVacancyFilter(userSession);
                    int numVacancies = vacancies.size();
                    int allResponses = logic.getAllResponses(vacancies);
                    sendMessage(chatId, "Вы ввели: " + userSession + " Найдено вакансий: " + numVacancies + " и всего откликов: " + allResponses);
                    sendAfterFindKeyboard(chatId);
                }
            } else if (messageText.equals("Вывести вакансии")){
                userSession.setState(ConversationState.SHOW_VACATIONS);
                 if (vacancies.size() != 0){
                     vacancies.forEach(vacancy -> {
                            sendMessage(chatId,"Вакансия: " + vacancy.getName() + "\nСсылка: http://hh.ru/vacancy/" + vacancy.getId());
                        });
                 }
                 else {
                        sendMessage(chatId, "Вакансий по указанным параметрам не найдено");
                 }
            } else if (messageText.equals("Назад к опциям")){
                userSession.setState(ConversationState.CONVERSATION_STARTED);
                sendInitialKeyboard(chatId);
            }
        }
    }

    private void sendMessage(long chatId, String mes) {
        SendMessage message = SendMessage
                .builder()
                .text(mes)
                .chatId(String.valueOf(chatId))
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendInitialKeyboard(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .text("Виберите опцию:")
                .chatId(String.valueOf(chatId))
                .build();

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setSelective(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Найти вакансии");
        keyboard.add(row);

        replyMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(replyMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendNestedKeyboard(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .text("Выберите и введите параметры поиска:")
                .chatId(String.valueOf(chatId))
                .build();

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setSelective(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Город");
        row1.add("Название вакансии");
        row1.add("Зарплата");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Опыт");
        row2.add("Найти");

        keyboard.add(row1);
        keyboard.add(row2);

        replyMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(replyMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendExperienceKeyboard(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .text("Виберите опыт работы:")
                .chatId(String.valueOf(chatId))
                .build();

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setSelective(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Нет опыта");
        row1.add("Опыт от 1 до 3 лет");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Опыт от 3 до 6 лет");
        row2.add("Опыт больше 6 лет");

        keyboard.add(row1);
        keyboard.add(row2);

        replyMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(replyMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAfterFindKeyboard(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .text("Что делать дальше?")//change text?
                .chatId(String.valueOf(chatId))
                .build();

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setSelective(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Вывести вакансии");
        row1.add("Назад к опциям");

        keyboard.add(row1);
        replyMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(replyMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}