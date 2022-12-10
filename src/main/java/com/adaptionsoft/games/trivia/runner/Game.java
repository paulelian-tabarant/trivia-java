package com.adaptionsoft.games.trivia.runner;

public interface Game {
    boolean add(String playerName);

    void roll(int roll);

    boolean rightAnswer();

    boolean wrongAnswer();
}
