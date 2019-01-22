package com.pbelov.java.tg.geo_guess_bot;

public class UserMessages {
    String userName;
    public int messagesCount;

    public UserMessages(String key, int value) {
        userName = key;
        messagesCount = value;
    }

    @Override
    public String toString() {
        return userName + (": ") + messagesCount;
    }
}
