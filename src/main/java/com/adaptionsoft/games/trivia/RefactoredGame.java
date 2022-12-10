package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.adaptionsoft.games.trivia.Category.*;

public class RefactoredGame implements Game {
    public static final int PLACES_SIZE = 12;

    // TODO: change visibility to private
    public static final int QUESTIONS_PER_CATEGORY = 50;

    private final Category[] placesCategories = {POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK, POP, SCIENCE, SPORTS, ROCK};
    ArrayList<Player> players = new ArrayList<>();

    // TODO: create a question deck and Question object containing a category and a title
    LinkedList<Question> popQuestions = new LinkedList<>();
    LinkedList<Question> scienceQuestions = new LinkedList<>();
    LinkedList<Question> sportsQuestions = new LinkedList<>();
    LinkedList<Question> rockQuestions = new LinkedList<>();

    int currentPlayerIndex = 0;
    boolean isGettingOutOfPenaltyBox;

    private final Logger logger;

    public RefactoredGame(Logger logger) {
        for (int questionIndex = 0; questionIndex < QUESTIONS_PER_CATEGORY; questionIndex++) {
            popQuestions.addLast(new Question(POP, questionIndex));
            scienceQuestions.addLast(new Question(SCIENCE, questionIndex));
            sportsQuestions.addLast(new Question(SPORTS, questionIndex));
            rockQuestions.addLast(new Question(ROCK, questionIndex));
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

    public void roll(int rollResult) {
        logger.log(getCurrentPlayer() + " is the current player");
        logger.log("They have rolled a " + rollResult);

        if (getCurrentPlayer().isInPenaltyBox()) {
            if (canPlayerGetOutOfPenaltyBox(rollResult)) {
                isGettingOutOfPenaltyBox = true;
                logger.log(getCurrentPlayer() + " is getting out of the penalty box");
            } else {
                logger.log(getCurrentPlayer() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            }
        }

        movePlayer(rollResult);

        logger.log(getCurrentPlayer() + "'s new location is " + getCurrentPlayer().getPlace());
        logger.log("The category is " + getCurrentCategory());

        askQuestion();
    }

    public boolean rightAnswer() {
        if (getCurrentPlayer().isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
            setTurnToNextPlayer();
            return true;
        }

        if (getCurrentPlayer().isInPenaltyBox())
            logger.log("Answer was correct!!!!");
        else
            logger.log("Answer was corrent!!!!");

        getCurrentPlayer().incrementPurse();
        logger.log(getCurrentPlayer() + " now has " + getCurrentPlayer().getPurse() + " Gold Coins.");

        setTurnToNextPlayer();
        return getCurrentPlayer().didWin();
    }

    public boolean wrongAnswer() {
        logger.log("Question was incorrectly answered");
        logger.log(getCurrentPlayer() + " was sent to the penalty box");
        getCurrentPlayer().moveToPenaltyBox();

        setTurnToNextPlayer();
        return true;
    }

    private boolean canPlayerGetOutOfPenaltyBox(int rollResult) {
        return rollResult % 2 != 0;
    }

    private void movePlayer(int rollResult) {
        getCurrentPlayer().moveForward(rollResult);
    }

    private void askQuestion() {
        if (getCurrentCategory().equals(POP))
            logger.log(popQuestions.removeFirst().getTitle());
        if (getCurrentCategory().equals(SCIENCE))
            logger.log(scienceQuestions.removeFirst().getTitle());
        if (getCurrentCategory().equals(SPORTS))
            logger.log(sportsQuestions.removeFirst().getTitle());
        if (getCurrentCategory().equals(ROCK))
            logger.log(rockQuestions.removeFirst().getTitle());
    }

    private Category getCurrentCategory() {
        int playerPlace = getCurrentPlayer().getPlace();
        return placesCategories[playerPlace];
    }

    private int countPlayers() {
        return players.size();
    }

    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void setTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % countPlayers();
    }
}
