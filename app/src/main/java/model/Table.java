package model;

import java.util.ArrayList;

public class Table {

    private final ArrayList<CardPlayerPair> deals;
    private final Card.Suit tromf;
    private boolean isFirstTurn;

    public Table(Card.Suit tromf) {
        super();
        this.tromf = tromf;
        deals = new ArrayList<>();
        isFirstTurn = true;
    }

    public Table(Table table) {
        this(table.getTromf());
        deals.addAll(table.getDeals());
        isFirstTurn = table.isFirstTurn();
    }

    public void addCard(Card c, Player.PlayerID p) {
        deals.add(new CardPlayerPair(c, p));
        isFirstTurn = false;
    }

    public int getValue() {
        int value = 0;
        for (CardPlayerPair cpp : deals) {
            value += cpp.getCard().getNumber().getValue();
        }
        return value;
    }

    public Card getFirstCard() {
        if (deals.isEmpty()) {
            throw new IllegalStateException("Ain't got no cards on the table, chief!");
        }
        return deals.get(0).getCard();
    }

    public int size() {
        return deals.size();
    }

    public Player.PlayerID getWinner() {
        Card firstCard = getFirstCard();
        Player.PlayerID winnerTromf = null, winnerNormal = null;
        Card bestTromf = null, bestNormal = null;
        for (CardPlayerPair cpp : deals) {
            Card c = cpp.getCard();
            Player.PlayerID p = cpp.getPlayer();
            if (c.getSuit() == tromf) {
                if (bestTromf == null || bestTromf.compareTo(c) < 0) {
                    bestTromf = c;
                    winnerTromf = p;
                }
            }
            else {
                if (c.getSuit() != firstCard.getSuit()) {
                    continue;
                }
                if (bestNormal == null || bestNormal.compareTo(c) < 0) {
                    bestNormal = c;
                    winnerNormal = p;
                }
             }
        }
        if (bestTromf != null) {
            return winnerTromf;
        }
        return winnerNormal;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    @Override
    public String toString() {
        return "Table{" +
                "deals=" + deals +
                ", tromf=" + tromf +
                '}';
    }

    public ArrayList<CardPlayerPair> getDeals() {
        return deals;
    }

    public Card.Suit getTromf() {
        return tromf;
    }
}
