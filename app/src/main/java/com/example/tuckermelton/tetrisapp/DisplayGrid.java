package com.example.tuckermelton.tetrisapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tuckermelton on 2/18/18.
 */

//Class to actually draw the grid on the screen.
public class DisplayGrid extends View {

    Paint paint = new Paint();
    TGrid tGrid = new TGrid(11, 24);
    TGrid previewTGrid = new TGrid(5, 5);
    final int BRICK_GAP_SIZE = 5;
    final int BRICK_SIZE = 54;
    final int PREVIEW_BRICK_SIZE = 50;
    final int PREVIEW_WINDOW_SIZE = previewTGrid.getHeight();
    boolean preview;

    public DisplayGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DisplayGrid,
                0, 0);

        try {
            preview = a.getBoolean(R.styleable.DisplayGrid_preview, false);
        } finally {
            a.recycle();
        }
    }

    //Method gets run after every invalidate() method call.
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int width = tGrid.getWidth();
        int height = tGrid.getHeight();

        //clear the background
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(this.paint);

        //draw a grid
        if(!preview){
            for (int row = 0; row < width; row++){
                for (int column = 0; column < height; column++){
                    if(tGrid.getCellAt(row, column) != null){
                        paint.setColor(tGrid.getCellAt(row, column).getColor());
                    }
                    else{
                        paint.setColor(Color.GRAY);
                    }
                    canvas.drawRect(
                            (BRICK_GAP_SIZE+BRICK_SIZE)*(row - 1),
                            (BRICK_GAP_SIZE+BRICK_SIZE)*(column - 1),
                            (BRICK_GAP_SIZE+BRICK_SIZE)*(row) - BRICK_GAP_SIZE,
                            (BRICK_GAP_SIZE+BRICK_SIZE)*(column) - BRICK_GAP_SIZE,
                            paint);
                }
            }
        }
        else{
            for (int row = 0; row < PREVIEW_WINDOW_SIZE; row++){
                for (int column = 0; column < PREVIEW_WINDOW_SIZE; column++){
                    if(previewTGrid.getCellAt(row, column) != null){
                        paint.setColor(previewTGrid.getCellAt(row, column).getColor());
                    }
                    else{
                        paint.setColor(Color.GRAY);
                    }
                    canvas.drawRect(
                            (BRICK_GAP_SIZE+PREVIEW_BRICK_SIZE)*(column - 1),
                            (BRICK_GAP_SIZE+PREVIEW_BRICK_SIZE)*(row - 1),
                            (BRICK_GAP_SIZE+PREVIEW_BRICK_SIZE)*(column) - BRICK_GAP_SIZE,
                            (BRICK_GAP_SIZE+PREVIEW_BRICK_SIZE)*(row) - BRICK_GAP_SIZE,
                            paint);
                }
            }
        }

    }

}
