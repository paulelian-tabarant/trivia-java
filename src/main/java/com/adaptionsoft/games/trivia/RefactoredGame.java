package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.adaptionsoft.games.trivia.Category.*;

public class RefactoredGame implements Game {
    public static final int PLACES_SIZE = 11;
    public static final int QUESTIONS_PER_CATEGORY = 50;
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
        for (int questionIndex = 0; questionIndex < QUESTIONS_PER_CATEGORY; questionIndex++) {
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
        logger.log("The category is " + getCurrentCategory());

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
        if (getCurrentCategory().equals(POP))
            logger.log(popQuestions.removeFirst());
        if (getCurrentCategory().equals(SCIENCE))
            logger.log(scienceQuestions.removeFirst());
        if (getCurrentCategory().equals(SPORTS))
            logger.log(sportsQuestions.removeFirst());
        if (getCurrentCategory().equals(ROCK))
            logger.log(rockQuestions.removeFirst());
    }


    private Category getCurrentCategory() {
        int playerPlace = places[currentPlayerIndex];
        return placesCategories[playerPlace];
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayerIndex] && !isGettingOutOfPenaltyBox) {
            nextPlayer();
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

        nextPlayer();

        return didPlayerWin();
    }


    public boolean wrongAnswer() {
        logger.log("Question was incorrectly answered");
        logger.log(players.get(currentPlayerIndex) + " was sent to the penalty box");
        inPenaltyBox[currentPlayerIndex] = true;

        nextPlayer();
        return true;
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playersNumber();
    }

    private boolean didPlayerWin() {
        return !(purses[currentPlayerIndex] == 6);
    }
}
