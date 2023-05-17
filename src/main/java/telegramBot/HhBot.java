package telegramBot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class HhBot extends TelegramLongPollingBot {
    UserSession userSession = new UserSession();
    private ConversationState state = null;
    public ReplyKeyboardMarkup buildMainMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Найти вакансии");

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    @Override
    public String getBotUsername() {
        return "hh_stat_study_bot";
    }

    @Override
    public String getBotToken() {
        return "5966702180:AAFGqL4F265UlYey_LowaNacrbRA6tkCAD8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            userSession.setChatId(chatId);

            if (messageText.equals("/start")) {
                state = ConversationState.CONVERSATION_STARTED;
                sendInitialKeyboard(chatId);
            } else if (messageText.equals("Найти вакансии")) {
                state = ConversationState.SET_PARAMETERS;
                sendNestedKeyboard(chatId);
            } else if (messageText.equals("Город/область")){
                state = ConversationState.SET_AREA_NAME;
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
                state = ConversationState.SET_PARAMETERS;
            } else if (messageText.equals("Название вакансии")){
                state = ConversationState.SET_VACATION_NAME;
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
                state = ConversationState.SET_PARAMETERS;
            } else if (messageText.equals("Опыт")){
                state = ConversationState.SET_EXPERIENCE;
                SendMessage message = SendMessage
                        .builder()
                        .text("Введите опыт:")
                        .chatId(String.valueOf(chatId))
                        .build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (state == ConversationState.SET_EXPERIENCE){
                userSession.setExperience(messageText);
                state = ConversationState.SET_PARAMETERS;
            } else if (messageText.equals("Найти")){
                state = ConversationState.SHOW_VACATIONS;
                SendMessage message = SendMessage
                        .builder()
                        .text("Вы ввели: " + userSession.toString())
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

}

