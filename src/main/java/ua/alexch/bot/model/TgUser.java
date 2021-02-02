package ua.alexch.bot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tg_user")
public class TgUser extends AbstractEntity {
    @Column(name = "user_id", unique = true, nullable = false)
    @NotNull
    private int userId;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "surname")
//    @NotBlank
    private String surname;

//    @Column(name = "bot_state", nullable = false)
//    @NotBlank
//    @Enumerated(EnumType.STRING)
//    private BotState botState;

    public TgUser() {
    }

    public TgUser(int id, String name, String surname) {
        this.userId = id;
        this.name = name;
        this.surname = surname;
//        this.botState = BotState.START;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

//    public BotState getBotState() {
//        return botState;
//    }
//
//    public void setBotState(BotState botState) {
//        this.botState = botState;
//    }
}
