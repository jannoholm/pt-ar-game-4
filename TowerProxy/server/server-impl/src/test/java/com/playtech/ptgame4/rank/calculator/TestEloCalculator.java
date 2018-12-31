package com.playtech.ptgame4.rank.calculator;

import com.playtech.ptargame4.server.rank.calculator.EloCalculatorImpl;
import com.playtech.ptargame4.server.rank.calculator.PlayerScore;
import com.playtech.ptargame4.server.rank.calculator.ScoreCriteria;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestEloCalculator {

    private enum Winner {
        TEAM1,
        TEAM2,
        DRAW
    }

	private static EloCalculatorImpl calculator;

    @BeforeClass
	public static void init() {
		calculator = new EloCalculatorImpl();
	}

	private PlayerScore generateScore(int userId, int elo, int towerHealth, int bridgesBuilt, int bridgesDestroyed, int enemykills, int saves, int deaths, int enemysaves) {
		Map<ScoreCriteria, Integer> map = new HashMap<>();
		map.put(ScoreCriteria.TOWER_HEALTH, towerHealth);
		map.put(ScoreCriteria.BRIDGES_BUILT, bridgesBuilt);
		map.put(ScoreCriteria.BRIDGES_DESTROYED, bridgesDestroyed);
		map.put(ScoreCriteria.BRIDGE_SOLDIER_ENEMY_KILLS, enemykills);
		map.put(ScoreCriteria.BRIDGE_SOLDIER_SAVES, saves);
		map.put(ScoreCriteria.BRIDGE_SOLDIER_DEATH, deaths);
		map.put(ScoreCriteria.BRIDGE_SOLDIER_ENEMY_SAVES, enemysaves);
		return new PlayerScore(userId, elo, map);
	}

	private void checkWinnerBasics(List<PlayerScore> team1, List<PlayerScore> team2, Winner winner) {
        double team1Rating = calculator.getTeamRating(team1);
        double team2Rating = calculator.getTeamRating(team2);

        // do calculations
        calculator.calculatePlayerPoints(team1, team2);

        switch (winner) {
            case TEAM1:
                assertTrue("Winner elo increased", calculator.getTeamRating(team1) > team1Rating);
                assertTrue("Loser elo decreased", calculator.getTeamRating(team2) < team2Rating);
                break;
            case TEAM2:
                assertTrue("Loser elo decreased", calculator.getTeamRating(team1) < team1Rating);
                assertTrue("Winner elo increased", calculator.getTeamRating(team2) > team2Rating);
                break;
            case DRAW:
                if (team1Rating > team2Rating) {
                    assertTrue("Bigger elo might be decreased", calculator.getTeamRating(team1) <= team1Rating);
                    assertTrue("Smaller elo might be increased", calculator.getTeamRating(team2) >= team2Rating);
                } else if (team1Rating < team2Rating) {
                    assertTrue("Bigger elo might be decreased", calculator.getTeamRating(team2) <= team2Rating);
                    assertTrue("Smaller elo might be increased", calculator.getTeamRating(team1) >= team1Rating);
                } else {
                    assertTrue("Elo should not change", calculator.getTeamRating(team1) == team1Rating);
                    assertTrue("Elo should not change", calculator.getTeamRating(team2) == team2Rating);
                }
                break;
        }
        assertTrue("Total points should not change", calculator.getTeamRating(team1) + calculator.getTeamRating(team2) == team1Rating + team2Rating);
    }

	@Test
	public void TestCalculatorWinLose() {
	    // some variables to be used
        List<PlayerScore> team1;
        List<PlayerScore> team2;
        double team1Rating;
        double team2Rating;

        // basic checks
        team1 = Arrays.asList(generateScore(10, 1000, 5, 4, 3, 23, 13, 7, 5));
        team2 = Arrays.asList(generateScore(20, 1000, 0, 6, 4, 12, 3, 23, 10));
        checkWinnerBasics(team1, team2, Winner.TEAM1);

        // test that order does not matter
        team1 = Arrays.asList(generateScore(10, 1000, 5, 4, 3, 23, 13, 7, 5));
        team2 = Arrays.asList(generateScore(20, 1000, 0, 6, 4, 12, 3, 23, 10));
        checkWinnerBasics(team2, team1, Winner.TEAM2);

        // test draw
        team1 = Arrays.asList(generateScore(10, 1000, 5, 4, 3, 23, 13, 7, 5));
        team2 = Arrays.asList(generateScore(20, 1000, 5, 6, 4, 12, 3, 23, 10));
        checkWinnerBasics(team2, team1, Winner.DRAW);

        // initialize baseline (no testing)
        List<PlayerScore> team1baseline = Arrays.asList(generateScore(10, 1000, 5, 4, 3, 23, 13, 7, 5));
        List<PlayerScore> team2baseline = Arrays.asList(generateScore(20, 1000, 0, 6, 4, 12, 3, 23, 10));
        double team1baselineRating = calculator.getTeamRating(team1baseline);
        double team2baselineRating = calculator.getTeamRating(team2baseline);
        calculator.calculatePlayerPoints(team1baseline, team2baseline);

		// test win by person with bigger elo leads to smaller increase
        team1 = Arrays.asList(generateScore(10, 1020, 5, 4, 3, 23, 13, 7, 5));
        team2 = Arrays.asList(generateScore(20, 980, 0, 6, 4, 12, 3, 23, 10));
        team1Rating = calculator.getTeamRating(team1);
        checkWinnerBasics(team1, team2, Winner.TEAM1);
        assertTrue("Bigger elo win leads to less points", calculator.getTeamRating(team1baseline)- team1baselineRating > calculator.getTeamRating(team1)-team1Rating);

        // test win by smaller elo leads to bigger point diff
        team1 = Arrays.asList(generateScore(10, 1020, 0, 4, 3, 23, 13, 7, 5));
        team2 = Arrays.asList(generateScore(20, 980, 5, 6, 4, 12, 3, 23, 10));
        team2Rating = calculator.getTeamRating(team2);
        checkWinnerBasics(team1, team2, Winner.TEAM2);
        assertTrue("Smaller elo win leads to more points", calculator.getTeamRating(team2baseline)- team2baselineRating < calculator.getTeamRating(team2)-team2Rating);
	}

}
