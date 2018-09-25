package com.example.tuckermelton.tetrisapp;

import java.util.ArrayList;

/**
 * Created by tuckermelton on 2/18/18.
 */

//Actual pieces on the grid.
public class Tetromino extends TGrid {

    protected TGrid parent;
    protected int xPos;
    protected int yPos;

    public Tetromino(int columns, int rows) {
        super(columns,rows);
        this.xPos = -1;
        this.yPos = -1;
    }

    //Parent grid sets the cells position.
    @Override
    public void putCell(int X, int Y, TCell cell) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return;
        }
        this.grid.get(X).set(Y, cell);
    }

    //Attempt to insert this Tetromino into a larger grid
    //Returns true if successful, otherwise it returns false
    public boolean insertIntoGrid(int X, int Y, TGrid parent) {
        if (parent == null) return false;
        //ensure that every spot we need is empty and is on the grid
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                if(this.getCellAt(iii,jjj) != null) {
                    if(X+iii < 1 || X+iii >= parent.getWidth() ||
                            Y+jjj < 1 || Y+jjj >= parent.getHeight() ||
                            parent.getCellAt(X+iii,Y+jjj) != null) {
                        return false;
                    }
                }
            }
        }

        //Actually insert the Tetromino
        for(int iii = 0; iii < this.rows; iii++) {
            for(int jjj = 0; jjj < this.columns; jjj++) {
                TCell nextCell = this.getCellAt(iii,jjj);
                if(nextCell != null) {
                    parent.putCell(X + iii, Y + jjj, nextCell);
                }
            }
        }

        this.xPos = X;
        this.yPos = Y;

        this.parent = parent;
        return true;
    }

    //Can we insert Tertromino at x,y?
    public boolean canInsert(int X, int Y, TGrid parent){
        if (parent == null) return false;
        //ensure that every spot we need is empty and is on the grid
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                if(this.getCellAt(iii,jjj) != null) {
                    if(X+iii < 1 || X+iii >= parent.getWidth() ||
                            Y+jjj < 1 || Y+jjj >= parent.getHeight() ||
                            parent.getCellAt(X+iii,Y+jjj) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Remove tetromino from Grid
    public void removeFromGrid() {
        if(this.parent == null) return;
        for(int iii = 0; iii < this.rows; iii++) {
            for(int jjj = 0; jjj < this.columns; jjj++) {
                TCell nextCell = this.getCellAt(iii,jjj);
                if(nextCell != null) {
                    parent.removeCell(nextCell.getXPosition(), nextCell.getYPosition());
                }
            }
        }
        this.parent = null;
        this.xPos = -1;
        this.yPos = -1;
    }


    //Shift the tetromino down
    public boolean shiftDown() {
        TGrid myParent = this.parent;
        int X = this.xPos;
        int Y = this.yPos;
        this.removeFromGrid();
        boolean result = this.insertIntoGrid(X, Y+1, myParent);
        //If we fail to move it down then put it back where it was
        if(!result) {
            this.insertIntoGrid(X, Y, myParent);
        }
        return result;
    }

    //Shift the tetrominio right
    public boolean shiftRight() {
        TGrid myParent = this.parent;
        int X = this.xPos;
        int Y = this.yPos;
        this.removeFromGrid();
        boolean result = this.insertIntoGrid(X+1, Y, myParent);
        //If we fail to move it then put it back where it was
        if(!result) {
            this.insertIntoGrid(X, Y, myParent);
        }
        return result;
    }

    //Shift the tetromino left
    public boolean shiftLeft() {
        TGrid myParent = this.parent;
        int X = this.xPos;
        int Y = this.yPos;
        this.removeFromGrid();
        boolean result = this.insertIntoGrid(X-1, Y, myParent);
        //If we fail to move it down then put it back where it was
        if(!result) {
            this.insertIntoGrid(X, Y, myParent);
        }
        return result;
    }

    public boolean rotateCounterClockwise() {
        //Square tetrominos don't rotate.
        if(this.columns != this.rows) {
            return true;
        }

        TGrid myParent = this.parent;
        int X = this.xPos;
        int Y = this.yPos;
        this.removeFromGrid();

        //Take the transpose
        ArrayList<ArrayList<TCell>> transpose = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            transpose.add(nextColumn);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                transpose.get(iii).set(jjj, this.grid.get(jjj).get(iii));
            }
        }

        //Reverse each row
        ArrayList<ArrayList<TCell>> rotated = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            rotated.add(nextColumn);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                rotated.get(iii).set(jjj, transpose.get(iii).get(this.columns - jjj - 1));
            }
        }

        ArrayList<ArrayList<TCell>> oldGrid = this.grid;

        this.grid = rotated;

        boolean result = this.insertIntoGrid(X, Y, myParent);

        //If we fail we can't give up so easily, try a "wall kick"
        if(!result) {
            //Shift 1 to the right
            result = this.insertIntoGrid(X+1, Y, myParent);
            if(!result) {
                //Shift 2 to the right
                result = this.insertIntoGrid(X+2, Y, myParent);
                if(!result) {
                    //Shift 1 to the left
                    result = this.insertIntoGrid(X-1, Y, myParent);
                    if(!result) {
                        //Shift 2 to the left
                        result = this.insertIntoGrid(X-2, Y, myParent);
                    }
                }
            }
        }

        //If we fail to move it down then put it back where it was
        if(!result) {
            this.grid = oldGrid;
            this.insertIntoGrid(X, Y, myParent);
        }
        return result;
    }

    public boolean rotateClockwise() {
        //Square Tetromino doesn't rotate.
        if(this.columns != this.rows) {
            return true;
        }

        TGrid myParent = this.parent;
        int X = this.xPos;
        int Y = this.yPos;
        this.removeFromGrid();

        //Take the transpose
        ArrayList<ArrayList<TCell>> transpose = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            transpose.add(nextColumn);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                transpose.get(iii).set(jjj, this.grid.get(jjj).get(iii));
            }
        }

        //Reverse each column
        ArrayList<ArrayList<TCell>> rotated = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            rotated.add(nextColumn);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                rotated.get(iii).set(jjj, transpose.get(this.columns - iii - 1).get(jjj));
            }
        }

        ArrayList<ArrayList<TCell>> oldGrid = this.grid;

        this.grid = rotated;

        boolean result = this.insertIntoGrid(X, Y, myParent);

        //If we fail we can't give up so easily, try a "wall kick"
        if(!result) {
            //Shift 1 to the right
            result = this.insertIntoGrid(X+1, Y, myParent);
            if(!result) {
                //Shift 2 to the right
                result = this.insertIntoGrid(X+2, Y, myParent);
                if(!result) {
                    //Shift 1 to the left
                    result = this.insertIntoGrid(X-1, Y, myParent);
                    if(!result) {
                        //Shift 2 to the left
                        result = this.insertIntoGrid(X-2, Y, myParent);
                    }
                }
            }
        }

        //If we fail to move it down then put it back where it was
        if(!result) {
            this.grid = oldGrid;
            this.insertIntoGrid(X, Y, myParent);
        }
        return result;

    }

    //Attempt to move down as far as possible
    public void zoomDown() {
        boolean result = true;
        while(result) {
            result = this.shiftDown();
        }
    }

}
