package com.example.crucecoach;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Card;
import model.CardPlayerPair;
import model.Model;
import model.Player;
import model.Round;
import model.RoundEngine;
import model.Table;
import model.Team;

public class MainActivity extends AppCompatActivity {
    
    private List<ImageView> suitClickers;
    private Round round;
    private List<ImageView> playerCardSprites;
    private List<ImageView> tableCardSprites;
    private List<TextView> tableCardText;
    private TextView scoreKeeper;
    private final Player.PlayerID humanPlayer = Player.PlayerID.T1P1;

    private final View.OnClickListener handListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = getResources().getResourceName(view.getId());
            String[] segms = name.split("myCard");
            int index = segms[segms.length - 1].charAt(0) - '1';
            if (handleHumanTurn(index)) {
                view.setVisibility(ImageView.GONE);
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Invalid move!");
                alertDialog.setMessage("Pay more attention.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, ">:(",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    };

    private final View.OnClickListener continueListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playTurn();
        }
    };

    private final View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    };
    
    private final View.OnClickListener tromfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = getResources().getResourceName(view.getId());
            hideImageElements(suitClickers);
            String[] segms = name.split("/");
            name = segms[segms.length - 1];
            switch (name) {
                case "inima":
                    beginPlay(Card.Suit.INIMA);
                    break;
                case "frunza":
                    beginPlay(Card.Suit.FRUNZA);
                    break;
                case "bota":
                    beginPlay(Card.Suit.BOTA);
                    break;
                case "ghinda":
                    beginPlay(Card.Suit.GHINDA);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
    }

    private void initialise() {
        playerCardSprites = getPlayerCards();
        tableCardSprites = getTableCards();
        hideImageElements(tableCardSprites);
        tableCardText = getTableText();
        hideTextElements(tableCardText);
        findViewById(R.id.restartButton).setOnClickListener(restartListener);
        scoreKeeper = (TextView) findViewById(R.id.score);

        Player.PlayerID firstTurn = humanPlayer;
        round = new Round(new Team(Team.TeamID.TEAM1), new Team(Team.TeamID.TEAM2), firstTurn);

        round.dealRound();
        Player p1 = round.getTeam1().getP1();

        for (int i = 0; i < p1.getCards().size(); i++) {
            playerCardSprites.get(i).setImageResource(findDrawable(p1.getCards().get(i)));
        }

        suitClickers = getSuitClickers();

        for (ImageView iv : suitClickers) {
            iv.setOnClickListener(tromfListener);
        }
    }
    
    public void beginPlay(Card.Suit tromf) {
        round.dealRound(tromf);
        Player p1 = round.getTeam1().getP1();
        for (int i = 0; i < p1.getCards().size(); i++) {
            playerCardSprites.get(i).setImageResource(findDrawable(p1.getCards().get(i)));
        }
        findViewById(R.id.continueButton).setOnClickListener(continueListener);
        playTurn();
    }

    private void playTurn() {
        if (round.getTable().getDeals().size() == Model.PLAYER_COUNT) {
            round.resetTable();
            hideImageElements(tableCardSprites);
            hideTextElements(tableCardText);
        }
        if (round.getViableCards().isEmpty()) {
            return;
        }
        if (round.getTurn() == humanPlayer) {
            playHuman();
            updateScore();
        }
        else {
            playComputer();
            updateScore();
            if (round.getTurn() == humanPlayer) {
                playTurn();
            }
        }
    }

    private void updateScore() {
        scoreKeeper.setText("// SCORE //\n" + round.getTeam1().getCurrentValue()
                + "(T1) - " + round.getTeam2().getCurrentValue() + " (T2)");
    }

    private void playHuman() {
        addHandAdvice();
        addHandListeners();
    }

    private void playComputer() {
        Card bestPlay = RoundEngine.getBestMove(round);
        putOnTable(bestPlay);
    }

    @SuppressLint("ResourceAsColor")
    private void addHandAdvice() {
        Card bestPlay = RoundEngine.getBestMove(round);
        System.out.println("Best play:" + bestPlay.toString());
        int index = round.getTeam1().getP1().getCards().indexOf(bestPlay);
        ImageView iv = playerCardSprites.get(arrayIndexToHandIndex(index));
        iv.setColorFilter(Color.argb(50, 0, 0, 0));
    }

    private void addHandListeners() {
        for (ImageView iv : playerCardSprites) {
            iv.setOnClickListener(handListener);
        }
    }
    
    private void removeHandListeners() {
        for (ImageView iv : playerCardSprites) {
            iv.setOnClickListener(null);
        }
    }

    private int handIndexToArrayIndex(int index) {
        int realIndex = 0;
        for (int i = 0; i <= index; i++) {
            if (playerCardSprites.get(i).getVisibility() == View.GONE) {
                continue;
            }
            realIndex++;
        }
        return realIndex - 1;
    }

    private int arrayIndexToHandIndex(int index) {
        int realIndex = -1;
        index++; // return index-th visible element
        while (index > 0) {
            realIndex++;
            if (playerCardSprites.get(realIndex).getVisibility() != View.GONE) {
                index--;
            }
        }
        return realIndex;
    }

    private boolean handleHumanTurn(int index) {
        Player p1 = round.getTeam1().getP1();
        int realIndex = handIndexToArrayIndex(index);
        Card choice = p1.getCards().get(realIndex);
        if (!round.isValidMove(choice)) {
            return false;
        }
        removeHandListeners();
        putOnTable(choice);
        playTurn();
        return true;
    }

    private void putOnTable(Card c) {
        round.dealCardNoReset(c);
        Table table = round.getTable();
        System.out.println(table);
        ArrayList<CardPlayerPair> deals = table.getDeals();
        for (int index = 0; index < deals.size(); index++) {
            CardPlayerPair cpp = deals.get(index);
            Player.PlayerID tablePlayer = cpp.getPlayer();
            Card tableCard = cpp.getCard();
            TextView textElement = tableCardText.get(index);
            ImageView cardElement = tableCardSprites.get(index);
            cardElement.setImageResource(findDrawable(tableCard));
            String newCaption = tablePlayer.toString();
            if (tablePlayer == humanPlayer) {
                newCaption = "YOU";
            }
            textElement.setText(newCaption);
            cardElement.setVisibility(ImageView.VISIBLE);
            textElement.setVisibility(ImageView.VISIBLE);

        }
    }

    private List<ImageView> getPlayerCards() {
        ImageView card1 = (ImageView) findViewById(R.id.myCard1);
        ImageView card2 = (ImageView) findViewById(R.id.myCard2);
        ImageView card3 = (ImageView) findViewById(R.id.myCard3);
        ImageView card4 = (ImageView) findViewById(R.id.myCard4);
        ImageView card5 = (ImageView) findViewById(R.id.myCard5);
        ImageView card6 = (ImageView) findViewById(R.id.myCard6);
        return Arrays.asList(card1, card2, card3, card4, card5, card6);
    }

    private List<ImageView> getTableCards() {
        ImageView table1 = (ImageView) findViewById(R.id.table1);
        ImageView table2 = (ImageView) findViewById(R.id.table2);
        ImageView table3 = (ImageView) findViewById(R.id.table3);
        ImageView table4 = (ImageView) findViewById(R.id.table4);
        return Arrays.asList(table1, table2, table3, table4);
    }

    private List<TextView> getTableText() {
        TextView table1text = (TextView) findViewById(R.id.table1text);
        TextView table2text = (TextView) findViewById(R.id.table2text);
        TextView table3text = (TextView) findViewById(R.id.table3text);
        TextView table4text = (TextView) findViewById(R.id.table4text);
        return Arrays.asList(table1text, table2text, table3text, table4text);
    }

    private List<ImageView> getSuitClickers() {
        ImageView bota = (ImageView) findViewById(R.id.bota);
        ImageView inima = (ImageView) findViewById(R.id.inima);
        ImageView frunza = (ImageView) findViewById(R.id.frunza);
        ImageView ghinda = (ImageView) findViewById(R.id.ghinda);
        return Arrays.asList(bota, inima, frunza, ghinda);
    }

    public void hideTextElements(List<TextView> els) {
        for (TextView el : els) {
            el.setVisibility(TextView.GONE);
        }
    }

    public void hideImageElements(List<ImageView> els) {
        for (ImageView el : els) {
            el.setVisibility(ImageView.GONE);
        }
    }

    public int findDrawable(Card c) {
        Card.Suit s = c.getSuit();
        Card.Number num = c.getNumber();
        int i;
        switch (s) {
            case INIMA:
                i = 1;
                break;
            case BOTA:
                i = 2;
                break;
            case FRUNZA:
                i = 3;
                break;
            default:
                i = 4;
        }
        int j;
        switch (num) {
            case PATRAR:
                j = 1;
                break;
            case TREIER:
                j = 2;
                break;
            case DOIER:
                j = 3;
                break;
            case ZACA:
                j = 4;
                break;
            case NOUAR:
                j = 5;
                break;
            default:
                j = 6;
        }
        String file = "row_" + i + "_column_" + j;
        return getDrawableFromString(file);
    }

    public int getDrawableFromString(String file) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(file);
            return field.getInt(null);
        } catch(Exception e) {
            Log.e("MyTag", "Failure to get drawable id.", e);
            return 0;
        }
    }
}