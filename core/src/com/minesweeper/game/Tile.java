package com.minesweeper.game;

public class Tile {

    public enum State {
        HIDDEN,
        FLAGGED,
        REVEALED
    }

    private boolean isMine;

    private State currState;

    private int adjacentMines;

    public Tile(boolean isMine) {
        this.isMine = isMine;
        this.adjacentMines = 0;
    }

    public void render() {
        switch (currState) {
            case HIDDEN:
                break;
            case FLAGGED:
                break;
            case REVEALED:
                break;
        }
    }

    public boolean isMine() {
        return isMine;
    }

    public State getState() {
        return currState;
    }

    public void reveal() {
        if(currState == State.HIDDEN) {
            currState = State.REVEALED;
        }
    }

    public void flag() {
        if(currState == State.HIDDEN) {
            currState = State.FLAGGED;
        }
    }

    public void unflag() {
        if(currState == State.FLAGGED) {
            currState = State.HIDDEN;
        }
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setState(State state) {
        currState = state;
    }

    
}
