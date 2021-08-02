package model;

import java.util.Objects;

public class Card implements Comparable<Card> {

    private final Number number;
    private final Suit suit;

    public Card(Number value, Suit suit) {
        this.number = value;
        this.suit = suit;
    }

    public enum Suit {
        BOTA, INIMA, FRUNZA, GHINDA
    }

    public enum Number {
        NOUAR(0), DOIER(2), TREIER(3),
        PATRAR(4), ZACA(10), AS(11);

        private final int value;

        Number(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Number getNumber() {
        return number;
    }

    public Suit getSuit() {
        return suit;
    }

    public static Card getAnnouncementComplement(Card c) {
        if (c.getNumber() == Number.TREIER) {
            return new Card(Number.PATRAR, c.getSuit());
        }
        if (c.getNumber() == Number.PATRAR) {
            return new Card(Number.TREIER, c.getSuit());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + number +
                ", suit=" + suit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number.equals(card.number) && suit.equals(card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, suit);
    }

    @Override
    public int compareTo(Card card) {
        return this.getNumber().compareTo(card.getNumber());
    }
}
