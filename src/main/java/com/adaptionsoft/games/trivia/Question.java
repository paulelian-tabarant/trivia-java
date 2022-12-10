package com.adaptionsoft.games.trivia;

public class Question {
    private final Category category;
    private final int index;

    public Question(Category category, int index) {
        this.category = category;
        this.index = index;
    }

    public String getTitle() {
        return category + " Question " + index;
    }
}
