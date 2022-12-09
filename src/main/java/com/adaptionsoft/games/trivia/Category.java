package com.adaptionsoft.games.trivia;

enum Category {
    POP("Pop"), SCIENCE("Science"), SPORTS("Sports"), ROCK("Rock");
    private final String name;
    Category(String name) { this.name = name; }
    @Override
    public String toString() { return name; }
}
