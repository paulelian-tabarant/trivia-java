package com.adaptionsoft.games.trivia;

public class StringLogger implements Logger {
    private String messages = "";

    @Override
    public void log(String message) {
        messages += message + '\n';
    }

    public String getMessages() {
        return messages;
    }
}
