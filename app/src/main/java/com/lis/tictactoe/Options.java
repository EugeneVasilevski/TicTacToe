package com.lis.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class Options extends AppCompatActivity
        implements
        SeekBar.OnSeekBarChangeListener {

    private final int NUM_OF_FIELD_SIZE = 5;

    private int fieldSize = 3;

    private TextView textView;
    private SeekBar seekBar;
    private CheckBox square;
    private CheckBox bot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        textView = (TextView) findViewById(R.id.textView);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        square = (CheckBox) findViewById(R.id.square);
        bot = (CheckBox) findViewById(R.id.bot);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        fieldSize = (int) Math.round(seekBar.getProgress() / (100.0 / (NUM_OF_FIELD_SIZE - 1)));
        fieldSize = 3 + fieldSize * 2;
        textView.setText(Integer.toString(fieldSize) + "x" + (Integer.toString(fieldSize)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void start(View view) {
        Intent intent = new Intent(Options.this, MainActivity.class);

        intent.putExtra("fieldSize", fieldSize);
        intent.putExtra("square", square.isChecked());
        intent.putExtra("vsBot", bot.isChecked());

        startActivity(intent);
    }
}
