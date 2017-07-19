package com.lis.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    protected RelativeLayout relativeLayout;
    protected RelativeLayout.LayoutParams params;
    protected TextView text;
    protected TextView text1;
    protected PlayingField view;
    protected GameLogic logic;
    protected BotLogic bot;
    protected Shadow shadow;
    protected boolean game_move;
    protected boolean click_block;
    protected int active_color;

    private Intent intent;
    private int fieldSize;
    private boolean square;
    private boolean vsBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main);

        intent = getIntent();
        fieldSize = intent.getIntExtra("fieldSize", 9);
        square = intent.getBooleanExtra("square", false);
        vsBot = intent.getBooleanExtra("vsBot", false);

        Display display = getWindowManager().getDefaultDisplay();
        relativeLayout = (RelativeLayout) findViewById(R.id.main);
        text = (TextView) findViewById(R.id.textView);
        text1 = (TextView) findViewById(R.id.textView1);
        view = (PlayingField) findViewById(R.id.playingField);
        view.initialization(display.getWidth(), display.getHeight(), fieldSize, square); // переделать
        shadow = (Shadow) findViewById(R.id.shadow);
        shadow.initialization(view.num_intermediate_cells, view.NUM_EXT_CELLS, view.cells_size,
                              view.stroke_width, display.getHeight(), view.field_size);
        params = (RelativeLayout.LayoutParams) findViewById(R.id.playingField).getLayoutParams();
        params.width = view.field_size;
        params.height = view.field_size;
        logic = new GameLogic(view.NUM_CELLS, view.NUM_EXT_CELLS);
        bot = new BotLogic();
        game_move = true;
        click_block = false;
        view.setOnTouchListener(this);
        setDrawable();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!click_block) {
                    if (vsBot) {
                        playerVsBot(x, y);
                    } else {
                        playerVsPlayer(x, y);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        String str;
        if (logic.a < bot.b) {
            str = "Поражение";
        } else {
            str = "Победа";
        }
        builder.setTitle(str)
                .setMessage("Ваш счет " + Integer.toString(logic.a))
                .setCancelable(false)
                .setNegativeButton("Restart",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                startActivity(intent);
                            }
                        })
                .setNeutralButton("Menu",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                startActivity(new Intent(MainActivity.this, Options.class));
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setDrawable() {
        relativeLayout.setBackgroundResource(R.drawable.shape);
    }

    private void playerVsPlayer(int x, int y) {
        click_block = true;
        if (game_move) {
            active_color = Color.rgb(255,87,137);
        } else {
            active_color = Color.rgb(248,222,102);
        }
        int score = humanMove(x, y);
        if (score != -1) {
            if (score == 0) {
                if (game_move) {
                    game_move = false;
                } else {
                    game_move = true;
                }
            } else {
                if (game_move) {
                    logic.a += score;
                    text.setText(Integer.toString(logic.a));
                } else {
                    logic.b += score;
                    text1.setText(Integer.toString(logic.b));
                }
            }
        }
        click_block = false;
    }

    private void playerVsBot(int x, int y) {
        click_block = true;
        active_color = Color.rgb(255,87,137);
        int score = humanMove(x, y);
        if (score != -1) {
            if (score == 0) {
                active_color = Color.rgb(248,222,102);
                botMove();
            } else {
                logic.a += score;
                text.setText(Integer.toString(logic.a));
                if (logic.isGameOver()) {
                    createDialog();
                }
            }
        }
        click_block = false;
    }

    public int humanMove(int x, int y) {
        int score;
        int[] indices = view.getIndexByClick(x, y);
        logic.x = indices[0];
        logic.y = indices[1];
        logic.z = indices[2];
        score = countScore();
        view.invalidate();
        return score;
    }

    public void botMove() {
        int score;
        while (true) {
            if (!bot.activeMove()) bot.randomMove();
            score = countScore();
            if (score == 0) break;
            else {
                bot.b += score;
            }
            if (logic.isGameOver()) {
                createDialog();
                break;
            }
        }
        view.invalidate();
        text1.setText(Integer.toString(bot.b));
    }

    private int countScore() {
        int score = 0;
        if (logic.checkStrokeWithInitialization()) {
            view.drawSide(logic.x, logic.y, logic.z);
            if (logic.auxiliaryGameArray[logic.x][logic.y] == 4) {
                view.drawCell(logic.x, logic.y, active_color);
                score++;
            }
            logic.setCoordAdjacentCell();
            if (logic.checkStrokeWithInitialization()) {
                if (logic.auxiliaryGameArray[logic.x][logic.y] == 4) {
                    view.drawCell(logic.x, logic.y, active_color);
                    score++;
                }
            }
            return score;
        }
        return -1;
    }

}


