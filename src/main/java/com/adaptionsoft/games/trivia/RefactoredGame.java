package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.adaptionsoft.games.trivia.Category.*;

public class RefactoredGame implements Game {
    public static final int PLACES_SIZE = 11;
    private final Category[] placesCategories = {POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK};
    ArrayList<String> players = new ArrayList<>();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    LinkedList<String> popQuestions = new LinkedList<>();
    LinkedList<String> scienceQuestions = new LinkedList<>();
    LinkedList<String> sportsQuestions = new LinkedList<>();
    LinkedList<String> rockQuestions = new LinkedList<>();

    int currentPlayerIndex = 0;
    boolean isGettingOutOfPenaltyBox;

    private final Logger logger;

    public RefactoredGame(Logger logger) {
        for (int questionIndex = 0; questionIndex < 50; questionIndex++) {
            popQuestions.addLast(createQuestion(POP, questionIndex));
            scienceQuestions.addLast(createQuestion(SCIENCE, questionIndex));
            sportsQuestions.addLast(createQuestion(SPORTS, questionIndex));
            rockQuestions.addLast(createQuestion(ROCK, questionIndex));
        }

        this.logger = logger;
    }

    public String createQuestion(Category category, int index) {
        return category + " Question " + index;
    }

    // TODO: create a Player object
    public boolean add(String playerName) {
        players.add(playerName);
        places[playersNumber()] = 0;
        purses[playersNumber()] = 0;
        inPenaltyBox[playersNumber()] = false;

        logger.log(playerName + " was added");
        logger.log("They are player number " + players.size());
        return true;
    }

    private int playersNumber() {
        return players.size();
    }

    public void roll(int result) {
        logger.log(players.get(currentPlayerIndex) + " is the current player");
        logger.log("They have rolled a " + result);

        if (inPenaltyBox[currentPlayerIndex]) {
            if (canPlayerGetOutOfPenaltyBox(result)) {
                isGettingOutOfPenaltyBox = true;
                logger.log(players.get(currentPlayerIndex) + " is getting out of the penalty box");
            } else {
                logger.log(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            }
        }

        movePlayer(result);

        logger.log(players.get(currentPlayerIndex)
                + "'s new location is "
                + places[currentPlayerIndex]);
        logger.log("The category is " + currentCategory());

        askQuestion();
    }

    private boolean canPlayerGetOutOfPenaltyBox(int result) {
        return result % 2 != 0;
    }

    private void movePlayer(int result) {
        places[currentPlayerIndex] += result;
        if (places[currentPlayerIndex] > PLACES_SIZE) places[currentPlayerIndex] -= (PLACES_SIZE + 1);
    }

    private void askQuestion() {
        if (currentCategory().equals(POP))
            logger.log((String) popQuestions.removeFirst());
        if (currentCategory().equals(SCIENCE))
            logger.log((String) scienceQuestions.removeFirst());
        if (currentCategory().equals(SPORTS))
            logger.log((String) sportsQuestions.removeFirst());
        if (currentCategory().equals(ROCK))
            logger.log((String) rockQuestions.removeFirst());
    }


    private Category currentCategory() {
        int playerPlace = places[currentPlayerIndex];
        return placesCategories[playerPlace];
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayerIndex] && !isGettingOutOfPenaltyBox) {
            movePlayerToNextPlace();
            return true;
        }

        if (inPenaltyBox[currentPlayerIndex])
            logger.log("Answer was correct!!!!");
        else
            logger.log("Answer was corrent!!!!");

        purses[currentPlayerIndex]++;
        logger.log(players.get(currentPlayerIndex)
                + " now has "
                + purses[currentPlayerIndex]
                + " Gold Coins.");

        movePlayerToNextPlace();

        return didPlayerWin();
    }

    private void movePlayerToNextPlace() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
    }

    public boolean wrongAnswer() {
        logger.log("Question was incorrectly answered");
        logger.log(players.get(currentPlayerIndex) + " was sent to the penalty box");
        inPenaltyBox[currentPlayerIndex] = true;

        movePlayerToNextPlace();
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayerIndex] == 6);
    }
}
