package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.trivia.runner.Game;
import com.adaptionsoft.games.uglytrivia.LegacyGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefactoredTriviaTest {
    private final StringLogger legacyStringLogger = new StringLogger();
    private final StringLogger refactoredStringLogger = new StringLogger();

    public void runWithFullCoverage(Game game) {
        game.add("Chet");
        game.add("Pat");
        game.add("Sue");

        for (int roll = 0; roll < 100; roll++) {
            game.roll(roll % 9);
            if (roll % 2 == 0) game.wrongAnswer();
            else game.rightAnswer();
        }
    }

    @Test
    public void shouldBehaveAsLegacyBehaves() {
        // Given
        LegacyGame legacyGame = new LegacyGame(legacyStringLogger);
        RefactoredGame game = new RefactoredGame(refactoredStringLogger);

        // When
        runWithFullCoverage(legacyGame);
        runWithFullCoverage(game);

        // Then
        assertEquals(legacyStringLogger.getMessages(), refactoredStringLogger.getMessages());
    }
}
