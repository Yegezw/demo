package com.zzw.match;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MatchRules {

    @Data
    public static class MatchInfo {
        /**
         * 选手 1
         */
        int id1;

        /**
         * 选手 2
         */
        int id2;
    }

    /**
     * 单循环
     *
     * @param n 队伍数量
     */
    public static LinkedHashMap<Integer, List<MatchInfo>> singleLoop(int n) {
        if (n < 2) {
            throw new IllegalArgumentException("n must be greater than 1");
        }

        boolean hasBye = n % 2 != 0;
        int participantCount = hasBye ? n + 1 : n;
        int byeId = participantCount;
        int[] teams = new int[participantCount];
        for (int i = 0; i < participantCount; i++) {
            teams[i] = i + 1;
        }

        LinkedHashMap<Integer, List<MatchInfo>> matchInfoMap = new LinkedHashMap<>();
        for (int round = 1; round <= participantCount - 1; round++) {
            List<MatchInfo> roundMatches = new ArrayList<>();
            for (int i = 0; i < participantCount / 2; i++) {
                int id1 = teams[i];
                int id2 = teams[participantCount - 1 - i];
                if (hasBye && (id1 == byeId || id2 == byeId)) {
                    continue;
                }
                MatchInfo matchInfo = new MatchInfo();
                matchInfo.id1 = id1;
                matchInfo.id2 = id2;
                roundMatches.add(matchInfo);
            }
            matchInfoMap.put(round, roundMatches);
            teams = rotate(teams);
        }
        return matchInfoMap;
    }

    private static int[] rotate(int[] teams) {
        int[] rotated = teams.clone();
        int last = teams[teams.length - 1];
        rotated[1] = last;
        for (int i = 2; i < teams.length; i++) {
            rotated[i] = teams[i - 1];
        }
        return rotated;
    }
}
