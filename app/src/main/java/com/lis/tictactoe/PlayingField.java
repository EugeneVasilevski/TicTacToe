package com.lis.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PlayingField extends View {
    int NUM_CELLS;
    int NUM_EXT_CELLS = 3;
    int field_size;
    int num_intermediate_cells;
    int cells_size;
    int stroke_width;
    private Canvas canvas;
    private Bitmap mBitmap;
    Paint paint;

    public PlayingField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialization(int screen_width, int screen_height, int fieldSize, boolean square) {
        NUM_CELLS = fieldSize;
        if (square) {
            NUM_EXT_CELLS = NUM_CELLS;
        }

        screen_height *= 0.6;
        field_size = Math.min(screen_width, screen_height);
        num_intermediate_cells = (NUM_CELLS - NUM_EXT_CELLS) / 2; // возможны проблемы с int. Уточнить значения!
        cells_size = field_size / NUM_CELLS;
        stroke_width = cells_size / 10;
        if (stroke_width % 2 != 0) stroke_width--;
        cells_size -= (stroke_width * 2) / NUM_CELLS;
        field_size = cells_size * NUM_CELLS + stroke_width; // необходим перерасчет. возможен выход за пределы BitMap
        mBitmap = Bitmap.createBitmap(field_size, field_size, Bitmap.Config.ARGB_8888); // узнать что это
        canvas = new Canvas(mBitmap);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(stroke_width);
        drawGrid();
        drawShadow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, paint);
    }

    public int[] getIndexByClick(int x, int y) {
        int[] arr = new int[3];
        int a = x / cells_size;
        int b = y / cells_size;
        int c = x - a * cells_size;
        int d = y - b * cells_size;
        int c1, d1, g;
        arr[0] = b;
        arr[1] = a;

        c1 = Math.abs(cells_size / 2 - c);
        d1 = Math.abs(cells_size / 2 - d);

        if (c1 > d1) {
            if (c > cells_size / 2) {
                g = 2;
            } else {
                g = 0;
            }
        } else {
            if (d > cells_size / 2) {
                g = 3;
            } else {
                g = 1;
            }
        }
        arr[2] = g;
        return arr;
    }

    public void drawSide(int y, int x, int k) {
        x *= cells_size;
        y *= cells_size;
        x += stroke_width / 2;
        y += stroke_width / 2;
        int a = x, b = y;
        paint.setColor(Color.WHITE);

        switch (k) {
            case 0:
                b += cells_size;
                break;
            case 1:
                a += cells_size;
                break;
            case 2:
                x += cells_size;
                a += cells_size;
                b += cells_size;
                break;
            case 3:
                y += cells_size;
                a += cells_size;
                b += cells_size;
                break;
            default:
                break;
        }
        canvas.drawLine(x, y, a, b, paint);
    }

    public void drawCell(int y, int x, int color) {
        x *= cells_size;
        y *= cells_size;
        x += stroke_width;
        y += stroke_width;
        paint.setColor(color);
        canvas.drawRect(x, y, x + cells_size - stroke_width, y + cells_size - stroke_width, paint);
    }

    public void drawShadow() {
        paint.setColor(Color.rgb(73,157,214));
        if (num_intermediate_cells == 0) {
            canvas.drawRect(stroke_width, stroke_width, cells_size, cells_size, paint);
        } else {
            int x = cells_size * num_intermediate_cells + stroke_width;
            int y = stroke_width;
            for (int i = 0; i < num_intermediate_cells + 1; i++) {
                canvas.drawRect(x, y, x + cells_size - stroke_width, y + cells_size - stroke_width, paint);
                x -= cells_size;
                y += cells_size;
            }
        }
    }

    protected void drawGrid() {
        int x = num_intermediate_cells * cells_size, y = stroke_width / 2;
        int x1 = y, y1 = x;
        int shift;

        for(int i = 0; i < num_intermediate_cells; i++) {
            shift = (NUM_EXT_CELLS + i * 2) * cells_size;
            drawMarkup(x, y, x1, y1, shift);
            drawBorders(x, y, x1, y1, shift);
            x -= cells_size;
            y += cells_size;
            x1 += cells_size;
            y1 -= cells_size;
        }
        for (int i = 0; i < NUM_EXT_CELLS + 1; i++) {
            shift = NUM_CELLS * cells_size;
            drawMarkup(x, y, x1, y1, shift);
            if (i == 0 || i == NUM_EXT_CELLS) {
                drawBorders(x, y, x1, y1, shift);
            }
            y += cells_size;
            x1 += cells_size;
        }
        for (int i = 0; i < num_intermediate_cells; i++) {
            x += cells_size;
            y1 += cells_size;
            //shift = (NUM_CELLS - 2 - i * 2) * cells_size + stroke_width;
            shift = (NUM_CELLS - 2 - i * 2) * cells_size;
            drawMarkup(x, y, x1, y1, shift);
            drawBorders(x, y, x1, y1, shift);
            y += cells_size;
            x1 += cells_size;
        }
        x = stroke_width / 2; y = num_intermediate_cells * cells_size;

        canvas.drawLine(x, y, x, y + NUM_EXT_CELLS * cells_size, paint);
        canvas.drawLine(field_size - stroke_width / 2, y, field_size - stroke_width / 2, y + NUM_EXT_CELLS * cells_size, paint);
        canvas.drawLine(y, x, y + NUM_EXT_CELLS * cells_size, x, paint);
        canvas.drawLine(y, field_size - stroke_width / 2, y + NUM_EXT_CELLS * cells_size, field_size - stroke_width / 2, paint);
    }

    private void drawMarkup(int x, int y, int x1, int y1, int shift) {
        paint.setColor(Color.rgb(150, 211, 255));
        canvas.drawLine(x, y, x + shift, y, paint);
        canvas.drawLine(x1, y1, x1, y1 + shift, paint);
    }

    private void drawBorders(int x, int y, int x1, int y1, int shift) {
        paint.setColor(Color.WHITE);
        canvas.drawLine(x, y, x + cells_size + stroke_width, y, paint);
        canvas.drawLine(x + shift - cells_size, y, x + shift + stroke_width, y, paint);
        canvas.drawLine(x1, y1, x1, y1 + cells_size + stroke_width, paint);
        canvas.drawLine(x1, y1 + shift, x1, y1 + shift - cells_size, paint);
    }
}