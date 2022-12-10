package com.adaptionsoft.games.trivia;

import static com.adaptionsoft.games.trivia.RefactoredGame.PLACES_SIZE;

public class Player {
    private final String name;
    private int purse = 0;
    private int place = 0;
    private boolean isInPenaltyBox = false;

    public Player(String name) { this.name = name; }

    @Override
    public String toString() { return name; }

    public int getPlace() { return place; }
    public void moveForward(int steps) { place = (place + steps) % PLACES_SIZE; }

    public int getPurse() { return purse; }
    public void incrementPurse() { purse++; }

    public boolean isInPenaltyBox() { return isInPenaltyBox; }
    public void moveToPenaltyBox() { isInPenaltyBox = true; }

    public boolean didWin() { return purse != 6; }
}
