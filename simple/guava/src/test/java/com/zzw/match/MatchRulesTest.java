package com.zzw.match;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MatchRulesTest {

    @Test
    void singleLoopShouldCreateAllPairsForEvenTeams() {
        LinkedHashMap<Integer, List<MatchRules.MatchInfo>> rounds = MatchRules.singleLoop(4);
        assertEquals(3, rounds.size());

        Set<String> uniquePairs = new HashSet<>();
        int totalMatches = 0;
        for (Map.Entry<Integer, List<MatchRules.MatchInfo>> entry : rounds.entrySet()) {
            List<MatchRules.MatchInfo> matches = entry.getValue();
            assertEquals(2, matches.size());
            for (MatchRules.MatchInfo match : matches) {
                uniquePairs.add(pairKey(match.id1, match.id2));
                totalMatches++;
            }
        }

        assertEquals(6, totalMatches);
        assertEquals(6, uniquePairs.size());
    }

    @Test
    void singleLoopShouldSkipByeForOddTeams() {
        LinkedHashMap<Integer, List<MatchRules.MatchInfo>> rounds = MatchRules.singleLoop(5);
        assertEquals(5, rounds.size());

        Set<String> uniquePairs = new HashSet<>();
        int totalMatches = 0;
        for (Map.Entry<Integer, List<MatchRules.MatchInfo>> entry : rounds.entrySet()) {
            List<MatchRules.MatchInfo> matches = entry.getValue();
            assertEquals(2, matches.size());
            for (MatchRules.MatchInfo match : matches) {
                assertTrue(match.id1 >= 1 && match.id1 <= 5);
                assertTrue(match.id2 >= 1 && match.id2 <= 5);
                uniquePairs.add(pairKey(match.id1, match.id2));
                totalMatches++;
            }
        }

        assertEquals(10, totalMatches);
        assertEquals(10, uniquePairs.size());
    }

    @Test
    void singleLoopShouldRejectInvalidTeamCount() {
        assertThrows(IllegalArgumentException.class, () -> MatchRules.singleLoop(1));
        assertThrows(IllegalArgumentException.class, () -> MatchRules.singleLoop(0));
    }

    private static String pairKey(int id1, int id2) {
        int min = Math.min(id1, id2);
        int max = Math.max(id1, id2);
        return min + "-" + max;
    }
}
