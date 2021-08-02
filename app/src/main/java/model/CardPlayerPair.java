package model;

public class CardPlayerPair implements Comparable<CardPlayerPair> {

    private final Card card;
    private final Player.PlayerID player;

    public CardPlayerPair(Card card, Player.PlayerID player) {
        this.card = card;
        this.player = player;
    }

    public Card getCard() {
        return card;
    }

    public Player.PlayerID getPlayer() {
        return player;
    }

    @Override
    public int compareTo(CardPlayerPair cpp) {
        return this.getCard().compareTo(cpp.getCard());
    }

    @Override
    public String toString() {
        return "CardPlayerPair{" +
                "card=" + card +
                ", player=" + player +
                '}';
    }
}
