package model;

import java.util.Collections;
import java.util.Stack;

public class Deck {

    private Stack<Card> deck;

    public Deck() {
        deck = new Stack<>();
    }

    public Deck(Deck deck) {
        this();
        this.deck.addAll(deck.getCards());
    }

    public Stack<Card> getCards() {
        return deck;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public void fill() {
        for (Card.Number num : Card.Number.class.getEnumConstants()) {
            for (Card.Suit s : Card.Suit.class.getEnumConstants()) {
                deck.add(new Card(num, s));
            }
        }
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }
}
