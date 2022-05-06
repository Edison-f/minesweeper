package com.minesweeper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class UI {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private int windowHeight;
    private int windowWidth;

    public enum State {
        RESET,
        NONE
    }

    private State currState;
    private State lastState;

    Grid grid;

    private int counter;

    public UI(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, Grid grid) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        this.currState = State.NONE;

        this.grid = grid;

        this.counter = 0;
    }

    public void render() {
        shapeRenderer.begin(ShapeType.Filled);
        if(currState == State.RESET) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.GRAY);
        }
        shapeRenderer.rect(windowWidth / 20, windowHeight / 20, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(windowWidth / 20, windowHeight / 20, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();
    }

    public void processInputs(int x, int y) {
        if (x > windowWidth / 20 && x < windowWidth / 20 + windowWidth / 5 && y > windowHeight / 20 && y < windowHeight / 20 + windowHeight / 10) {
            currState = State.RESET;
        } else {
            currState = State.NONE;
        }
    }
    
    public void stateMachine() {
        switch (currState) {
            case RESET:
                if(lastState != currState) {
                    lastState = currState;
                    grid.resetGrid();
                    counter++;
                }
                lastState = State.RESET;
                break;
            case NONE:
                lastState = State.NONE;
                break;
        }
    }
}