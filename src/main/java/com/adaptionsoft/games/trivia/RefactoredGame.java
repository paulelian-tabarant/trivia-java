package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.adaptionsoft.games.trivia.Category.*;

public class RefactoredGame implements Game {
    // TODO: set to 12 and adapt code
    public static final int PLACES_SIZE = 11;
    public static final int QUESTIONS_PER_CATEGORY = 50;
    private final Category[] placesCategories = {POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK};
    ArrayList<Player> players = new ArrayList<>();

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

    public boolean add(String playerName) {
        players.add(new Player(playerName));

        logger.log(playerName + " was added");
        logger.log("They are player number " + players.size());
        return true;
    }

    private int playersNumber() {
        return players.size();
    }

    public void roll(int result) {
        logger.log(getCurrentPlayer() + " is the current player");
        logger.log("They have rolled a " + result);

        if (getCurrentPlayer().isInPenaltyBox()) {
            if (canPlayerGetOutOfPenaltyBox(result)) {
                isGettingOutOfPenaltyBox = true;
                logger.log(getCurrentPlayer() + " is getting out of the penalty box");
            } else {
                logger.log(getCurrentPlayer() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            }
        }

        movePlayer(result);

        logger.log(getCurrentPlayer()
                + "'s new location is "
                + getCurrentPlayer().getPlace());
        logger.log("The category is " + getCurrentCategory());

        askQuestion();
    }

    private boolean canPlayerGetOutOfPenaltyBox(int result) {
        return result % 2 != 0;
    }

    private void movePlayer(int result) {
        int newPlace = getCurrentPlayer().getPlace() + result;
        if (newPlace > PLACES_SIZE) newPlace -= (PLACES_SIZE + 1);
        getCurrentPlayer().setPlace(newPlace);
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
        int playerPlace = getCurrentPlayer().getPlace();
        return placesCategories[playerPlace];
    }

    public boolean wasCorrectlyAnswered() {
        if (getCurrentPlayer().isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
            nextPlayer();
            return true;
        }

        if (getCurrentPlayer().isInPenaltyBox())
            logger.log("Answer was correct!!!!");
        else
            logger.log("Answer was corrent!!!!");

        getCurrentPlayer().incrementPurse();
        logger.log(getCurrentPlayer()
                + " now has "
                + getCurrentPlayer().getPurse()
                + " Gold Coins.");

        nextPlayer();

        return didPlayerWin();
    }

    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }


    public boolean wrongAnswer() {
        logger.log("Question was incorrectly answered");
        logger.log(getCurrentPlayer() + " was sent to the penalty box");
        getCurrentPlayer().setInPenaltyBox(true);

        nextPlayer();
        return true;
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playersNumber();
    }

    private boolean didPlayerWin() {
        return !(getCurrentPlayer().getPurse() == 6);
    }
}
