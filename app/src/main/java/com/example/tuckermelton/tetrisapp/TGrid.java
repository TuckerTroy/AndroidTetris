package com.example.tuckermelton.tetrisapp;

import java.util.ArrayList;

/**
 * Created by tuckermelton on 2/18/18.
 */
public class TGrid {

    protected ArrayList<ArrayList<TCell>> grid;

    protected int columns;
    protected int rows;

    public TGrid(int columns, int rows) {

        this.columns = columns;
        this.rows = rows;

        //Generate an empty grid
        this.grid = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            this.grid.add(nextColumn);
        }

    }

    public int getWidth() {
        return this.columns;
    }

    public int getHeight() {
        return this.rows;
    }


    //Return a pointer to the cell at a position (X,Y)
    public TCell getCellAt(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return null;
        }
        return this.grid.get(X).get(Y);
    }

    //Same as getCellAt(X,Y), except this function also removes it from the grid
    public TCell extractCellAt(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return null;
        }
        TCell result = this.grid.get(X).get(Y);
        this.grid.get(X).set(Y, null);
        return result;
    }

    //Insert a cell into the grid
    public void putCell(int X, int Y, TCell cell) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return;
        }
        cell.setXPosition(X);
        cell.setYPosition(Y);
        this.grid.get(X).set(Y, cell);
    }

    //Remove a cell from the grid
    public void removeCell(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return;
        }
        TCell theCell = this.grid.get(X).get(Y);
        if(theCell != null) {
            this.grid.get(X).set(Y, null);
            theCell.setYPosition(-1);
            theCell.setXPosition(-1);
        }
    }

    //Look for the first row that is full
    //Returns -1 if not found
    public int getFirstFullRow() {
        boolean fullRow = false;
        int largest = 0;
        for(int jjj = 0; jjj < this.rows; jjj++) {
            for(int iii = 0; iii < this.columns; iii++) {
                int col = iii;
                int row = jjj;
                int count = 0;
                largest = 0;
                while(this.getCellAt(col, row) != null){
                    count++;
                    col++;
                    if(count == 10){
                        fullRow = true;
                    }
                }
            }
            if(fullRow) {
                return jjj;
            }
        }
        return -1;
    }

    //Deletes a row, shifts everything down
    public void deleteRow(int row) {
        for(int iii = 0; iii < this.columns; iii++) {
            this.removeCell(iii, row);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = row; jjj >= 0; jjj--) {
                TCell cellToMove = this.extractCellAt(iii, jjj - 1);
                if(cellToMove != null) {
                    this.putCell(iii, jjj, cellToMove);
                }
            }
        }
    }

    //Delete all cells
    public void clear() {
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                this.grid.get(iii).set(jjj, null);
            }
        }
    }

}
