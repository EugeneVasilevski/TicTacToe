package com.lis.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class Shadow extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Matrix matrix;
    private LinearGradient gradient;
    private Canvas canvas;
    private Bitmap bitmap1;
    private int x;

    public Shadow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialization(int num_intermediate_cells, int num_ext_cells, int cells_size,
                               int stroke_width, int screen_height, int field_size) {

        int width = (int) ((num_intermediate_cells + num_ext_cells) * cells_size * Math.pow(2.0, 0.5) + stroke_width * 1.6);
        int height = (field_size + (screen_height - field_size) / 2);

        if (num_intermediate_cells == 0) {
            x = ((num_ext_cells - 1) / 2) * cells_size;
            height += height / 4;
        } else {
            x = cells_size;
        }
        gradient = new LinearGradient(0, 0, 0, height, Color.rgb(73,157,214),
                Color.rgb(117,213,249), Shader.TileMode.REPEAT);
        paint = new Paint();
        matrix = new Matrix();
        paint.setDither(true);
        paint.setShader(gradient);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        matrix.postRotate(-45, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, width, height, paint);
        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap1, -x, -x, null);
    }
}
