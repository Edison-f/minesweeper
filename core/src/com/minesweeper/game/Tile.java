package com.minesweeper.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
        this.currState = State.HIDDEN;
    }

    public void render(int x, int y, int tileSize, ShapeRenderer shapeRenderer) {
        switch (currState) {
            case HIDDEN:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(x, y, tileSize, tileSize );
                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(x, y, tileSize, tileSize );
                shapeRenderer.end();
                break;
            case FLAGGED:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(x, y, tileSize, tileSize );
                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(x, y, tileSize, tileSize );
                shapeRenderer.end();
                break;
            case REVEALED:

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(x, y, tileSize, tileSize );
                shapeRenderer.end();
                if(isMine) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(x, y, tileSize, tileSize );
                    shapeRenderer.end();
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.valueOf("E7E7E7"));
                    shapeRenderer.rect(x, y, tileSize, tileSize );
                    shapeRenderer.end();
                }
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

    public void tapped() {
        if(currState == State.HIDDEN) {
            currState = State.REVEALED;
        }
    }
}
