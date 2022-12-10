package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.runner.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.adaptionsoft.games.trivia.Category.*;

// TODO: might be good to use aspect programming for logs!

public class RefactoredGame implements Game {
    public static final int PLACES_SIZE = 12;
    private static final int QUESTIONS_PER_CATEGORY = 50;

    private final Category[] categoriesByPlace = {
            POP, SCIENCE, SPORTS, ROCK,
            POP, SCIENCE, SPORTS, ROCK,
            POP, SCIENCE, SPORTS, ROCK
    };
    ArrayList<Player> players = new ArrayList<>();
    Map<Category, LinkedList<Question>> questionDeck = new HashMap<>();

    int currentPlayerIndex = 0;
    // TODO: possible bug to rectify here, would make sense having one boolean per player
    boolean isGettingOutOfPenaltyBox;

    private final Logger logger;

    public RefactoredGame(Logger logger) {
        for (Category category : Category.values()) {
            questionDeck.put(category, new LinkedList<>());

            for (int questionIndex = 0; questionIndex < QUESTIONS_PER_CATEGORY; questionIndex++) {
                questionDeck.get(category).add(new Question(category, questionIndex));
            }
        }

        this.logger = logger;
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
        if (getCurrentPlayer().isInPenaltyBox()) {
            if (!isGettingOutOfPenaltyBox) {
                setTurnToNextPlayer();
                return true;
            }

            logger.log("Answer was correct!!!!");
        }
        else {
            // TODO: possible bug to rectify here
            logger.log("Answer was corrent!!!!");
        }

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
        Question question = questionDeck.get(getCurrentCategory()).removeFirst();
        logger.log(question.getTitle());
    }

    private Category getCurrentCategory() {
        int playerPlace = getCurrentPlayer().getPlace();
        return categoriesByPlace[playerPlace];
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
