package com.adaptionsoft.games.trivia;

public class Player {
    private final String name;
    private int purse;
    private int place;

    public Player(String name) {
        this.name = name;
        this.place = 0;
        this.purse = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPlace() { return place; }
    public void setPlace(int newPlace) { place = newPlace; }

    public int getPurse() { return purse; }
    public void incrementPurse() { purse++; }
}
