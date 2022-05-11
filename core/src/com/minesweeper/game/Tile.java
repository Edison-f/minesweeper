package com.minesweeper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tile {

    public enum State {
        HIDDEN,
        FLAGGED,
        REVEALED
    }

    private boolean isMine;

    private State currState;

    private int adjacentMines = 999;

    private BitmapFont font;
    private SpriteBatch batch;

    private int windowHeight;
    private int windowWidth;

    public Tile(boolean isMine, BitmapFont font, SpriteBatch batch) {
        this.isMine = isMine;
        this.adjacentMines = 0;
        this.currState = State.HIDDEN;
        
        this.font = font;
        this.batch = batch;
    
        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();
    }

    public void renderTile(int x, int y, int tileSize, ShapeRenderer shapeRenderer, boolean isSelected) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if(!isSelected) {
            shapeRenderer.setColor(Color.BLACK);
        } else {
            shapeRenderer.setColor(Color.valueOf("0088ff"));
            Gdx.gl.glLineWidth(8);
        }
        shapeRenderer.rect(x, y, tileSize, tileSize);
        shapeRenderer.end();

        switch (currState) {
            case HIDDEN:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(x, y, tileSize, tileSize);
                shapeRenderer.end();
                break;
            case FLAGGED:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.CORAL);
                shapeRenderer.rect(x, y, tileSize, tileSize);
                shapeRenderer.end();
                break;
            case REVEALED:
                if(isMine) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(x, y, tileSize, tileSize);
                    shapeRenderer.end();
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    if(adjacentMines > 0){
                        shapeRenderer.setColor(new Color((1 - (adjacentMines / 8.0f)), (1 - (adjacentMines / 8.0f)), (1 - (adjacentMines / 8.0f)) * 0.2f, 1.0f));
                    } else {
                        shapeRenderer.setColor(Color.WHITE);
                    }
                    shapeRenderer.rect(x, y, tileSize, tileSize);
                    shapeRenderer.end();
                }
                break;
        }
        Gdx.gl.glLineWidth(2);        
    }

    public void renderNumbers(int x, int y) {
        if(currState == State.REVEALED && !isMine && adjacentMines > 0) { 
            batch.begin();
            font.draw(batch, "" + adjacentMines, x / 2, (y * -1 + windowHeight) / 2);
            batch.end();
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

    public boolean flag() {
        if(currState == State.HIDDEN) {
            currState = State.FLAGGED;
            return true;
        } else if(currState == State.FLAGGED) {
            currState = State.HIDDEN;
            return false;
        }
        return false;
    }

    public boolean isFlagged() {
        return currState == State.FLAGGED;
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

    public boolean isRevealed() {
        return currState == State.REVEALED;
    }
}
