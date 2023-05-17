package telegramBot.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import telegramBot.UserSession;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserSessionService {
    private Map<Long, UserSession> userSessionMap = new HashMap<>();

//    public UserSession getSession(Long chatId) {
//        return userSessionMap.getOrDefault(chatId, UserSession
//                .builder()
//                .chatId(chatId)
//                .build());
//    }

    public UserSession saveSession(Long chatId, UserSession session) {
        return userSessionMap.put(chatId, session);
    }
}
