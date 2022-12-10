package com.adaptionsoft.games.trivia;

public class Player {
    private final String name;
    private int purse = 0;
    private int place = 0;
    private boolean isInPenaltyBox = false;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPlace() { return place; }
    public void setPlace(int newPlace) { place = newPlace; }

    public int getPurse() { return purse; }
    public void incrementPurse() { purse++; }

    public boolean isInPenaltyBox() {
        return isInPenaltyBox;
    }

    public void setInPenaltyBox(boolean inPenaltyBox) {
        isInPenaltyBox = inPenaltyBox;
    }
}
