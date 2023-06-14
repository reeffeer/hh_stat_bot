package telegramBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.services.UserSessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HhBot extends TelegramLongPollingBot {
    UserSessionService userSessionService = new UserSessionService();

    //    {"noExperience","between1And3","between3And6","moreThan6"}
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
        return "1587428127:AAHk3cxFtUV87O6SR9Fhrj0gn8LVNANcNZ0";
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
            } else if (messageText.equals("Город/область")){
                userSession.setState(ConversationState.SET_AREA_NAME);
                SendMessage message = SendMessage
                        .builder()
                        .text("Введите название города/области:")
                        .chatId(String.valueOf(chatId))
                        .build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (state == ConversationState.SET_AREA_NAME){
                userSession.setCity(messageText);
                userSession.setState(ConversationState.SET_PARAMETERS);
            } else if (messageText.equals("Название вакансии")){
                userSession.setState(ConversationState.SET_VACATION_NAME);
                SendMessage message = SendMessage
                        .builder()
                        .text("Введите название вакансии:")
                        .chatId(String.valueOf(chatId))
                        .build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (state == ConversationState.SET_VACATION_NAME){
                userSession.setVacation(messageText);
                userSession.setState(ConversationState.SET_PARAMETERS);
            } else if (messageText.equals("Опыт")){
                userSession.setState(ConversationState.SET_EXPERIENCE);
                sendExperienceKeyboard(chatId);
            } else if (messageText.equals("Зарплата")){
                userSession.setState(ConversationState.SET_SALARY);
                SendMessage message = SendMessage
                        .builder()
                        .text("Введите зарплату:")
                        .chatId(String.valueOf(chatId))
                        .build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (state == ConversationState.SET_EXPERIENCE){
                String experienceId = experienceMap.get(messageText);
                if (experienceId != null){
                    userSession.setExperience(experienceId);
                    userSession.setState(ConversationState.SET_PARAMETERS);
                    sendNestedKeyboard(chatId);
                } else {
                    SendMessage message = SendMessage
                            .builder()
                            .text("Такого опыта не найдено. Выберите из предлагаемых вариантов:")
                            .chatId(String.valueOf(chatId))
                            .build();
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendExperienceKeyboard(chatId);
                }
            } else if (state == ConversationState.SET_SALARY){
                if (messageText.matches("-?\\d+")) {
                    userSession.setSalary(Integer.parseInt(messageText));
                    userSession.setState(ConversationState.SET_PARAMETERS);
                    sendNestedKeyboard(chatId);
                } else {
                    SendMessage message = SendMessage
                            .builder()
                            .text("Вы ввели некоректное число. Попробуйте снова: ")
                            .chatId(String.valueOf(chatId))
                            .build();
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            } else if (messageText.equals("Найти")){
                userSession.setState(ConversationState.SHOW_VACATIONS);
                SendMessage message = SendMessage
                        .builder()
                        .text("Вы ввели: " + userSession)
                        .chatId(String.valueOf(chatId))
                        .build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
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
                .text("Виберите и введите параметры поиска:")
                .chatId(String.valueOf(chatId))
                .build();

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setSelective(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Город/область");
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

}