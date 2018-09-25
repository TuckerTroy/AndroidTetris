package com.example.tuckermelton.tetrisapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by tuckermelton on 2/18/18.
 */

public class MainActivity extends AppCompatActivity implements TimeHandler.IUpdate {

    //UI Buttons
    Button reset;
    ImageButton left;
    ImageButton rotateLeft;
    ImageButton down;
    ImageButton rotateRight;
    ImageButton right;
    Toast gameOverToast;

    //Grid stuff
    DisplayGrid displayGrid;
    TGrid tGrid;
    DisplayGrid previewDisplayGrid;
    TGrid previewTGrid;
    boolean gameOver = false;
    ArrayList<Tetromino> activeTetrominos;
    ArrayList<Tetromino> testTetrominos;
    int index = 0;

    //Score stuff
    int rowsCleared;
    int currentScore;
    int currentLevel;
    TextView rows;
    TextView level;
    TextView score;
    TimeHandler timeHandler;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.levelUp){
            currentLevel++;
            String display = "" + currentLevel;

            timeHandler.rateLimitMillis = (int) (timeHandler.rateLimitMillis * .80);
            level.setText(display);
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup score stuff
        rowsCleared = 0;
        currentScore = 0;
        currentLevel = 0;
        rows = findViewById(R.id.rows);
        level = findViewById(R.id.level);
        score = findViewById(R.id.score);

        //Setup displayGrid stuff
        displayGrid = findViewById(R.id.displayGrid);
        tGrid = displayGrid.tGrid;
        previewDisplayGrid = findViewById(R.id.displayGridPreview);
        previewTGrid = previewDisplayGrid.previewTGrid;

        //Setup tetrominos
        testTetrominos = new ArrayList<Tetromino>();
        buildTestTetrominoList();
        activeTetrominos = new ArrayList<Tetromino>();
        setUpTetrominos();

        //Setup buttons
        reset = findViewById(R.id.reset);
        left = findViewById(R.id.left);
        rotateLeft = findViewById(R.id.rotateLeft);
        down = findViewById(R.id.down);
        rotateRight = findViewById(R.id.rotateRight);
        right = findViewById(R.id.right);
        gameOverToast = Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG);

        //On reset click
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver = false;
                rowsCleared = 0;
                currentScore = 0;
                currentLevel = 0;
                timeHandler.rateLimitMillis = 1000;
                index = 0;
                activeTetrominos.clear();
                setUpTetrominos();
                updateLevel();
                updateScore();
                tGrid.clear();
                previewTGrid.clear();
                displayGrid.invalidate();
                previewDisplayGrid.invalidate();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTetrominos.get(index).shiftLeft();
                displayGrid.invalidate();
            }
        });

        rotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTetrominos.get(index).rotateClockwise();
                displayGrid.invalidate();
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTetrominos.get(index).zoomDown();
                displayGrid.invalidate();
            }
        });

        rotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTetrominos.get(index).rotateCounterClockwise();
                displayGrid.invalidate();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTetrominos.get(index).shiftRight();
                displayGrid.invalidate();
            }
        });

        //This will handle per-second updates to the grid.
        timeHandler = new TimeHandler(this);

    }

    //Check if the row is full
    private void checkFullRow() {
        int fullRow = tGrid.getFirstFullRow();
        while(fullRow != -1){
            tGrid.deleteRow(fullRow);
            rowsCleared++;
            updateRowsCleared();
            updateLevel();
            updateScore();
            fullRow = tGrid.getFirstFullRow();
        }
    }

    private void updateRowsCleared() {
        String display = "" + rowsCleared;
        rows.setText(display);
    }

    private void updateLevel() {
        int newLevel = rowsCleared / 5;
        if(currentLevel < newLevel){
            currentLevel = newLevel;
            timeHandler.rateLimitMillis = (int) (timeHandler.rateLimitMillis * .80);
        }
        String display = "" + currentLevel;
        level.setText(display);
    }

    //Update the currentScore
    private void updateScore() {
        currentScore += currentLevel;
        String display = "" + currentScore;
        score.setText(display);
    }

    //Game is over if we can't insert pieces into the grid.
    private void checkGameOver() {
        for(Tetromino t : testTetrominos){
            if(t.canInsert(4, 1, tGrid)){
                gameOver = false;
            }
            else{
                gameOver = true;
            }
        }
    }

    private void buildTestTetrominoList(){
        testTetrominos.add(TetrominoBuilder.I());
        testTetrominos.add(TetrominoBuilder.J());
        testTetrominos.add(TetrominoBuilder.L());
        testTetrominos.add(TetrominoBuilder.O());
        testTetrominos.add(TetrominoBuilder.S());
        testTetrominos.add(TetrominoBuilder.Z());
        testTetrominos.add(TetrominoBuilder.T());
    }

    private void setUpTetrominos() {
        activeTetrominos.add(TetrominoBuilder.Random());
        activeTetrominos.get(0).insertIntoGrid(4, 1, tGrid);
        activeTetrominos.add(TetrominoBuilder.Random());
        activeTetrominos.get(1).insertIntoGrid(1, 2, previewTGrid);
    }

    //Method runs every second on the UIThread.
    @Override
    public void updateSeconds(int secondsValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!activeTetrominos.get(index).shiftDown()){
                    checkGameOver();
                    if(gameOver){
                        gameOverToast.show();
                    }
                    else{
                        checkFullRow();
                        index++;
                        activeTetrominos.get(index).insertIntoGrid(4, 1, tGrid);
                        activeTetrominos.add(TetrominoBuilder.Random());
                        previewTGrid.clear();
                        activeTetrominos.get(index + 1).insertIntoGrid(1, 2, previewTGrid);
                    }

                }
                displayGrid.invalidate();
                previewDisplayGrid.invalidate();
            }
        });
    }
}
