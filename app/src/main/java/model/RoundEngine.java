package model;

import java.util.ArrayList;

public class RoundEngine {

    private static final int INF = Integer.MAX_VALUE;
    private static final int MAX_DEPTH = 8;

    private static boolean shouldMaximise(Round r) {
        return r.getTeamTurn() == Team.TeamID.TEAM1;
    }

    private static CardValuePair backtrack(int depth, Round r, Card lastPlayed, boolean maximise) {
        Card bestMove = null;
        int bestScore = maximise? -INF : INF;
        ArrayList<Card> candidates = r.getViableCards();
        if (candidates.isEmpty() || depth == MAX_DEPTH) {
            return new CardValuePair(lastPlayed, r.getTeam1().getCurrentValue());
        }
        for (int i = 0; i < candidates.size(); i++) {
            Round backup = new Round(r);
            Card c = candidates.get(i);
            backup.dealCard(c);
            CardValuePair bestCardValue = backtrack(depth + 1, backup, c, shouldMaximise(backup));
            if (maximise ^ (bestCardValue.getValue() < bestScore)) {
                bestScore = bestCardValue.getValue();
                bestMove = c;
            }
        }
        return new CardValuePair(bestMove, bestScore);
    }

    public static Card getBestMove(Round r) {
        Round backup = new Round(r);
        CardValuePair bestPair = backtrack(0, backup, null, shouldMaximise(r));
        System.out.println(bestPair);
        return bestPair.getCard();
    }
}
