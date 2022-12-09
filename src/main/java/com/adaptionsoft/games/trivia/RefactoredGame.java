package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.LinkedList;

public class RefactoredGame implements Game {
    public static final int PLACES_SIZE = 11;
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
            popQuestions.addLast(createQuestion("Pop", questionIndex));
            scienceQuestions.addLast(createQuestion("Science", questionIndex));
            sportsQuestions.addLast(createQuestion("Sports", questionIndex));
            rockQuestions.addLast(createQuestion("Rock", questionIndex));
        }

        this.logger = logger;
    }

    public String createQuestion(String category, int index) {
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
            if (result % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                logger.log(players.get(currentPlayerIndex) + " is getting out of the penalty box");

                places[currentPlayerIndex] += result;
                if (places[currentPlayerIndex] > PLACES_SIZE) places[currentPlayerIndex] -= (PLACES_SIZE + 1);

                logger.log(players.get(currentPlayerIndex)
                        + "'s new location is "
                        + places[currentPlayerIndex]);
                logger.log("The category is " + currentCategory());
                askQuestion();
            } else {
                logger.log(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            places[currentPlayerIndex] = places[currentPlayerIndex] + result;
            if (places[currentPlayerIndex] > 11) places[currentPlayerIndex] = places[currentPlayerIndex] - 12;

            logger.log(players.get(currentPlayerIndex)
                    + "'s new location is "
                    + places[currentPlayerIndex]);
            logger.log("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        if (currentCategory().equals("Pop"))
            logger.log((String) popQuestions.removeFirst());
        if (currentCategory().equals("Science"))
            logger.log((String) scienceQuestions.removeFirst());
        if (currentCategory().equals("Sports"))
            logger.log((String) sportsQuestions.removeFirst());
        if (currentCategory().equals("Rock"))
            logger.log((String) rockQuestions.removeFirst());
    }


    private String currentCategory() {
        if (places[currentPlayerIndex] == 0) return "Pop";
        if (places[currentPlayerIndex] == 4) return "Pop";
        if (places[currentPlayerIndex] == 8) return "Pop";
        if (places[currentPlayerIndex] == 1) return "Science";
        if (places[currentPlayerIndex] == 5) return "Science";
        if (places[currentPlayerIndex] == 9) return "Science";
        if (places[currentPlayerIndex] == 2) return "Sports";
        if (places[currentPlayerIndex] == 6) return "Sports";
        if (places[currentPlayerIndex] == 10) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayerIndex]) {
            if (isGettingOutOfPenaltyBox) {
                logger.log("Answer was correct!!!!");
                purses[currentPlayerIndex]++;
                logger.log(players.get(currentPlayerIndex)
                        + " now has "
                        + purses[currentPlayerIndex]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayerIndex++;
                if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;

                return winner;
            } else {
                currentPlayerIndex++;
                if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
                return true;
            }


        } else {

            logger.log("Answer was corrent!!!!");
            purses[currentPlayerIndex]++;
            logger.log(players.get(currentPlayerIndex)
                    + " now has "
                    + purses[currentPlayerIndex]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayerIndex++;
            if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        logger.log("Question was incorrectly answered");
        logger.log(players.get(currentPlayerIndex) + " was sent to the penalty box");
        inPenaltyBox[currentPlayerIndex] = true;

        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayerIndex] == 6);
    }
}
