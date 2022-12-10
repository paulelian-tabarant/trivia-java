package com.adaptionsoft.games.trivia;

public class Player {
    private final String name;
    private int place;

    public Player(String name) {
        this.name = name;
        this.place = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int newPlace) {
        place = newPlace;
    }
}
