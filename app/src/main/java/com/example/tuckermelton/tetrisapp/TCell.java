package com.example.tuckermelton.tetrisapp;


/**
 * Created by tuckermelton on 2/18/18.
 */

//Represents a cell on the grid, methods self explanatory.
public class TCell {

    protected int myColor;
    protected int xPos;
    protected int yPos;

    public TCell(int myColor) {
        this.myColor = myColor;
        this.xPos = -1;
        this.yPos = -1;
    }

    public String toString() {
        String result = "(";
        result += this.xPos;
        result += ",";
        result += ")";
        return result;
    }

    public int getColor() {
        return this.myColor;
    }

    public void setColor(int newColor) {
        this.myColor = newColor;
    }

    public int getXPosition() {
        return xPos;
    }

    public void setXPosition(int xPos) {
        this.xPos = xPos;
    }

    public int getYPosition() {
        return yPos;
    }

    public void setYPosition(int yPos) {
        this.yPos = yPos;
    }
}
