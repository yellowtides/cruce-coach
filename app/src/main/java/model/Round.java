package model;

import java.util.ArrayList;

public class Round {

    private final Team team1, team2;
    private final Deck deck;
    private Table table;
    private Player.PlayerID turn;
    private Card.Suit tromf;
    private boolean isFirstTable;

    public Round(Team team1, Team team2, Player.PlayerID turn) {
        this.team1 = team1;
        this.team2 = team2;
        deck = new Deck();
        deck.fill();
        deck.shuffle();
        this.turn = turn;
        isFirstTable = true;
    }

    public Round(Round round) {
        team1 = new Team(round.getTeam1());
        team2 = new Team(round.getTeam2());
        deck = new Deck(round.getDeck());
        table = new Table(round.getTable());
        turn = round.getTurn();
        tromf = round.getTromf();
        isFirstTable = round.isFirstTable();
    }

    public void dealRound() {
        team1.dealEach(deck, Model.DRAW_SIZE);
        team2.dealEach(deck, Model.DRAW_SIZE);
    }

    public void dealRound(Card.Suit tromf) {
        dealRound();
        this.tromf = tromf;
        table = new Table(tromf);
    }

    public Card dealRandom() {
        Player turnPlayer = idToPlayer(turn);
        Card choice;
        if (this.isFirstTable()) {
            choice = turnPlayer.getRandom(tromf, tromf);
        }
        else {
            try {
                choice = turnPlayer.getRandom(table.getFirstCard().getSuit(), tromf);
            } catch (Exception e) {
                choice = turnPlayer.getRandom();
            }
        }
        dealCard(choice);
        return choice;
    }

    public ArrayList<Card> getViableCards() {
        Player turnPlayer = idToPlayer(turn);
        if (this.isFirstTable()) {
            return turnPlayer.getViableCards(tromf, tromf);
        }
        if (table.isFirstTurn()) {
            return turnPlayer.getCards();
        }
        return turnPlayer.getViableCards(table.getFirstCard().getSuit(), tromf);
    }

    public void dealCard(Card c) {
        dealCardNoReset(c);
        resetTable();
    }

    public boolean isValidMove(Card c) {
        return getViableCards().contains(c);
    }

    public void dealCardNoReset(Card c) {
        Player turnPlayer = idToPlayer(turn);
        if (!turnPlayer.hasCard(c)) {
            throw new IllegalArgumentException("Player doesn't own that card!");
        }
        Card maybeAnnouncement = Card.getAnnouncementComplement(c);
        if (table.isFirstTurn() && maybeAnnouncement != null && turnPlayer.hasCard(maybeAnnouncement)) {
            Team announcementTeam = getTeam(turn);
            if (maybeAnnouncement.getSuit() == tromf) {
                announcementTeam.addValue(Model.TROMF_ANNOUNCEMENT);
            }
            else {
                announcementTeam.addValue(Model.NORMAL_ANNOUNCEMENT);
            }
        }
        table.addCard(c, turn);
        idToPlayer(turn).removeCard(c);
        if (table.size() == Model.PLAYER_COUNT) {
            turn = handleWinner();
        } else {
            passTurn();
        }
        isFirstTable = false;
    }

    public void resetTable() {
        table = new Table(tromf);
    }

    public Team getTeam(Player.PlayerID p) {
        if (p == Player.PlayerID.T1P1 || p == Player.PlayerID.T1P2) {
            return team1;
        }
        return team2;
    }

    private Player.PlayerID handleWinner() {
        int value = table.getValue();
        Player.PlayerID winner = table.getWinner();
        getTeam(winner).addValue(value);
        return winner;
    }

    private void passTurn() {
        turn = getNext(turn);
    }

    public static Player.PlayerID getNext(Player.PlayerID current) {
        switch (current) {
            case T1P1:
                return Player.PlayerID.T2P1;
            case T2P1:
                return Player.PlayerID.T1P2;
            case T1P2:
                return Player.PlayerID.T2P2;
            default:
                return Player.PlayerID.T1P1;
        }

    }

    public Player idToPlayer(Player.PlayerID player) {
        switch (player) {
            case T1P1:
                return team1.getP1();
            case T1P2:
                return team1.getP2();
            case T2P1:
                return team2.getP1();
            default:
                return team2.getP2();
        }
    }

    @Override
    public String toString() {
        return "Round{" +
                "team1=" + team1 +
                ", team2=" + team2 +
                ", deck=" + deck +
                ", table=" + table +
                ", turn=" + turn +
                ", tromf=" + tromf +
                '}';
    }

    public boolean isFirstTable() {
        return isFirstTable;
    }

    public Player.PlayerID getTurn() {
        return turn;
    }

    public Team.TeamID getTeamTurn() {
        return getTeam(turn).getName();
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card.Suit getTromf() {
        return tromf;
    }

    public Table getTable() {
        return table;
    }
}
