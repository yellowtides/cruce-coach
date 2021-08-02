package model;

public class CardValuePair implements Comparable<CardValuePair> {

    private final Card card;
    private final Integer value;

    public CardValuePair(Card card, int value) {
        this.card = card;
        this.value = value;
    }

    public Card getCard() {
        return card;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(CardValuePair cvp) {
        return this.getValue().compareTo(cvp.getValue());
    }

    @Override
    public String toString() {
        return "CardValuePair{" +
                "card=" + card +
                ", value=" + value +
                '}';
    }
}
