package org.example.service;

import lombok.var;
import org.example.model.UserRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.example.config.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private static final String HELP_TEXT = "Этот бот создан для поиска вакансий на сайте HeadHunter\n\n" +
            "После авторизации Вам будут доступны дополнительные функции";


    public TelegramBot(BotConfig config){
        this.config = config;
        List <BotCommand>listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start","Поиск вакансий"));
        listOfCommands.add(new BotCommand("/auth","Авторизироваться"));
        listOfCommands.add(new BotCommand("/mydata","Мои данные"));
        listOfCommands.add(new BotCommand("/help","Как пользоваться ботом"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        }catch (TelegramApiException e){

        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
//    private void vacancyRequest(Message message){
//        long chatId = message.getChatId();
//        Chat chat = message.getChat();
//
//        UserRequest userRequest =new UserRequest();
//        userRequest.setChatId(chatId);
//        userRequest.getCity(chat.)
//
//    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&& update.getMessage().hasText()){
            UserRequest userRequest = new UserRequest();
            String messageText = update.getMessage().getText();
            long chatId =update.getMessage().getChatId();
            userRequest.setChatId(chatId);

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());

                    break;
                case "/auth":

                case "/mydata":

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                default:
                    sendMessage(chatId, "Sorry");
            }
        }

    }
    private void startCommandReceived(long chatId, String name){
        String answer = "Привет, "+name+" я помогу тебе найти свежие вакансии с сайта HeadHunter!\n" +
                "Для начала напиши вакансию которая тебя интересует.";
        sendMessage(chatId,answer);

    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.enableMarkdown(true);
        try{
            execute(message);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
}
