package telegramBot;

import lombok.Data;

@Data
public class UserSession {
    private Long chatId;
    private String vacation;
    private String city;
    private String experience;
    private int salary;
    private ConversationState state;

    public UserSession() {
        this.state = ConversationState.CONVERSATION_STARTED;
    }
}