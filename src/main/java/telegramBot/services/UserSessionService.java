package telegramBot.services;

import telegramBot.UserSession;

import java.util.HashMap;
import java.util.Map;

public class UserSessionService {
    private Map<Long, UserSession> userSessionMap = new HashMap<>();

    public UserSession getSession(Long chatId) {
        UserSession userSession = userSessionMap.get(chatId);
        if (userSession == null){
            userSession = new UserSession();
            userSessionMap.put(chatId, userSession);
            return userSession;
        }
        return userSession;
    }

    public UserSession saveSession(Long chatId, UserSession session) {
        return userSessionMap.put(chatId, session);
    }
}
