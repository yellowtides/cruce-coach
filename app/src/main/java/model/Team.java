package model;

public class Team {

    public enum TeamID {
        TEAM1, TEAM2
    }

    private final TeamID name;
    private final Player p1, p2;
    private int currentValue;

    public Team(TeamID name) {
        this.name = name;
        if (name == TeamID.TEAM1) {
            p1 = new Player(Player.PlayerID.T1P1);
            p2 = new Player(Player.PlayerID.T1P2);
        }
        else {
            p1 = new Player(Player.PlayerID.T2P1);
            p2 = new Player(Player.PlayerID.T2P2);
        }
        currentValue = 0;
    }

    public Team(Team team) {
        name = team.getName();
        p1 = new Player(team.getP1());
        p2 = new Player(team.getP2());
        currentValue = team.getCurrentValue();
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public void dealEach(Deck deck, int count) {
        p1.drawCards(deck, count);
        p2.drawCards(deck, count);
    }

    public void addValue(int value) {
        currentValue += value;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", currentValue=" + currentValue +
                '}';
    }

    public TeamID getName() {
        return name;
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
