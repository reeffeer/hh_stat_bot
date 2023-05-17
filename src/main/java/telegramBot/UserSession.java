package telegramBot;

import lombok.Data;

@Data
public class UserSession {
    private Long chatId;
    private String vacation;
    private String city;
    private String experience;
}