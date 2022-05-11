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

    private int resetOffsetX;
    private int resetOffsetY;

    private int dPadOffsetX;
    private int dPadOffsetY;

    private int dPadHeight;
    private int dPadWidth;

    public enum State {
        RESET,
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        INTERACT
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

        this.resetOffsetX = windowWidth - windowWidth / 2 - windowWidth / 10;
        this.resetOffsetY = windowHeight - windowHeight / 20;

        this.dPadOffsetX = windowWidth / 2 - windowWidth / 6;
        this.dPadOffsetY = windowHeight / 15;

        this.dPadWidth = windowWidth / 8;
        this.dPadHeight = windowHeight / 15;

    }

    public void render() {

        // Draw the reset button
        shapeRenderer.begin(ShapeType.Filled);
        if(currState == State.RESET) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.GRAY);
        }
        shapeRenderer.rect(resetOffsetX, resetOffsetY, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(resetOffsetX, resetOffsetY, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();

        // Draw the dpad
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(dPadOffsetX, dPadOffsetY, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 8, dPadOffsetY - windowHeight / 15, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 8, dPadOffsetY + windowHeight / 15, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 4, dPadOffsetY, dPadWidth, dPadHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(dPadOffsetX, dPadOffsetY, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 8, dPadOffsetY - windowHeight / 15, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 8, dPadOffsetY + windowHeight / 15, dPadWidth, dPadHeight);
        shapeRenderer.rect(dPadOffsetX + windowWidth / 4, dPadOffsetY, dPadWidth, dPadHeight);
        shapeRenderer.end();

        // Draw the interact button
        shapeRenderer.begin(ShapeType.Filled);
        if(currState != State.INTERACT) {
            shapeRenderer.setColor(Color.GRAY);
        } else {
            shapeRenderer.setColor(Color.YELLOW);
        }
        shapeRenderer.rect(windowWidth - windowWidth / 4, windowHeight / 20, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(windowWidth - windowWidth / 4, windowHeight / 20, windowWidth / 5, windowHeight / 10);
        shapeRenderer.end();
    }

    public void processInputs(int x, int y) {
        // Check if the user clicked on the reset button
        if (x > resetOffsetX && x < resetOffsetX + windowWidth / 5 && y > resetOffsetY && y <resetOffsetY + windowHeight / 10) {
            currState = State.RESET;
        } else {
            currState = State.NONE;
        }

        // Check if the user clicked on the dpad
        if(x > dPadOffsetX && x < dPadOffsetX + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight) {
            currState = State.LEFT;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY - windowHeight / 15 && y < dPadOffsetY - windowHeight / 15 + dPadHeight) {
            currState = State.DOWN;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY + windowHeight / 15 && y < dPadOffsetY + windowHeight / 15 + dPadHeight) {
            currState = State.UP;
        } else if(x > dPadOffsetX + windowWidth / 4 && x < dPadOffsetX + windowWidth / 4 + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight) {
            currState = State.RIGHT;
        } 

        // Check if the user clicked on the interact button
        if(x > windowWidth - windowWidth / 4 && x < windowWidth - windowWidth / 4 + windowWidth / 5 && y > windowHeight / 20 && y < windowHeight / 20 + windowHeight / 10) {
            currState = State.INTERACT;
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
            case UP:
                if(lastState != currState) {
                    lastState = currState;
                    grid.moveUp();
                }
                lastState = State.UP;
                break;
            case DOWN:
                if(lastState != currState) {
                    lastState = currState;
                    grid.moveDown();
                }
                lastState = State.DOWN;
                break;
            case LEFT:
                if(lastState != currState) {
                    lastState = currState;
                    grid.moveLeft();
                }
                lastState = State.LEFT;
                break;
            case RIGHT:
                if(lastState != currState) {
                    lastState = currState;
                    grid.moveRight();
                }
                lastState = State.RIGHT;
                break;
            case INTERACT:
                if(lastState != currState) {
                    lastState = currState;
                    grid.interact();
                }
            
            case NONE:
                lastState = State.NONE;
                break;
        }
    }
}