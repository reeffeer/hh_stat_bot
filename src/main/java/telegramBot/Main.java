package telegramBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Main {
    private static Logger log;
    static {
        log = Logger.getLogger("logger");
        log.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            log.log(Level.INFO, "Registering bot...");
            telegramBotsApi.registerBot(new HhBot());
        } catch (TelegramApiRequestException e) {
            log.log(Level.WARNING, "Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
        log.log(Level.INFO, "Telegram bot is ready to accept updates from user");
    }
}