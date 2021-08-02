package model;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    public enum PlayerID {
        T1P1, T2P1, T1P2, T2P2
    }

    private final PlayerID name;
    private final ArrayList<Card> cards;

    public Player(PlayerID name) {
        this.name = name;
        cards = new ArrayList<>();
    }

    public Player(Player player) {
        this(player.getName());
        cards.addAll(player.getCards());
    }

    public void drawCards(Deck deck, int count) {
        if (deck == null) {
            throw new NullPointerException("Given deck is null.");
        }
        if (count > deck.getCards().size()) {
            throw new IllegalArgumentException("Given count is larger than deck.");
        }
        for (int i = 0; i < count; i++) {
            cards.add(deck.getCards().pop());
        }
    }

    public Card getRandom(Card.Suit played, Card.Suit tromf) {
        return getRandom(getViableCards(played, tromf));
    }

    private static Card getRandom(ArrayList<Card> options) {
        Random random = new Random();
        int listSize = options.size();
        int randomIndex = random.nextInt(listSize);
        return options.get(randomIndex);
    }

    public ArrayList<Card> getViableCards(Card.Suit played, Card.Suit tromf) {
        ArrayList<Card> viableCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.getSuit() == played) {
                viableCards.add(c);
            }
        }
        if (!viableCards.isEmpty()) {
            return viableCards;
        }
        for (Card c : cards) {
            if (c.getSuit() == tromf) {
                viableCards.add(c);
            }
        }
        if (!viableCards.isEmpty()) {
            return viableCards;
        }
        return cards;
    }

    public void removeCard(Card c) {
        cards.remove(c);
    }

    public boolean hasCard(Card c) {
        return cards.contains(c);
    }

    public Card getRandom() {
        return getRandom(cards);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", cards=" + cards +
                '}';
    }

    public PlayerID getName() {
        return name;
    }
}
