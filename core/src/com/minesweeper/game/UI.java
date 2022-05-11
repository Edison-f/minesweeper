package com.minesweeper.game;

import java.security.acl.LastOwnerException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private int resetWidth;
    private int resetHeight;

    private int dPadOffsetX;
    private int dPadOffsetY;

    private int dPadHeight;
    private int dPadWidth;

    private int interactOffsetX;
    private int interactOffsetY;

    private int interactHeight;
    private int interactWidth;

    public enum State {
        RESET,
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        INTERACT,
        REVEAL,
        FLAG
    }

    private State currState;
    private State lastState;

    Grid grid;

    private float interactCounter;
    private boolean interactPressed;

    private int flagCounter;
    private int mineCount;

    public UI(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, Grid grid, int mineCount) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        this.currState = State.NONE;

        this.grid = grid;

        this.interactCounter = 0;
        
        this.resetWidth = windowWidth / 5;
        this.resetHeight = windowHeight / 10;

        this.resetOffsetX = windowWidth / 2 - resetWidth / 2;
        this.resetOffsetY = windowHeight - windowHeight / 20;

        this.dPadWidth = windowWidth / 8;
        this.dPadHeight = windowHeight / 15;

        this.dPadOffsetX = windowWidth / 2 - windowWidth / 3 - dPadWidth / 2;
        this.dPadOffsetY = windowHeight / 15;

        //windowWidth - windowWidth / 4, windowHeight / 20, windowWidth / 5, windowHeight / 10

        this.interactWidth = windowWidth / 5;
        this.interactHeight = windowHeight / 10;

        this.interactOffsetX = windowWidth / 2 + windowWidth /4 - interactWidth / 2;
        this.interactOffsetY = windowHeight / 20;

        this.mineCount = mineCount;
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
        if(currState != State.REVEAL || currState != State.FLAG) {
            shapeRenderer.setColor(Color.GRAY);
        } else {
            shapeRenderer.setColor(Color.YELLOW);
        }
        if(interactCounter > 0.25) {
            shapeRenderer.setColor(Color.GREEN);
        }
        shapeRenderer.rect(interactOffsetX, interactOffsetY, interactWidth, interactHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(interactOffsetX, interactOffsetY, interactWidth, interactHeight);
        shapeRenderer.end();

        // Draw the mine counter
        batch.begin();
        font.draw(batch, "Mines: " + (mineCount - flagCounter), 30, (10 * -1 + windowHeight) / 2);
        batch.end();
    }

    public void processInputs(int x, int y) {
        // Check if the user clicked on the reset button
        if (x > resetOffsetX && x < resetOffsetX + windowWidth / 5 && y > resetOffsetY && y < resetOffsetY + windowHeight / 10) {
            currState = State.RESET;
        } else if(currState != State.INTERACT && currState == lastState) {
            currState = State.NONE;
        }

        // Check if the user clicked on the dpad
        if(x > dPadOffsetX && x < dPadOffsetX + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.A)) {
            currState = State.LEFT;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY - windowHeight / 15 && y < dPadOffsetY - windowHeight / 15 + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.S)) {
            currState = State.DOWN;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY + windowHeight / 15 && y < dPadOffsetY + windowHeight / 15 + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.W)) {
            currState = State.UP;
        } else if(x > dPadOffsetX + windowWidth / 4 && x < dPadOffsetX + windowWidth / 4 + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.D)) {
            currState = State.RIGHT;
        } 

        // Check if the user clicked on the interact button
        if(x > interactOffsetX && x < interactOffsetX + interactWidth && y > interactOffsetY && y < interactOffsetY + interactHeight || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // currState = State.REVEAL;
            interactCounter += Gdx.graphics.getDeltaTime();
            interactPressed = true;
            currState = State.INTERACT;
        } else {
            interactPressed = false;
        }
    }
    
    public void stateMachine() {

        switch (currState) {
            case RESET:
                if(lastState != currState) {
                    lastState = currState;
                    grid.resetGrid();
                    mineCount = grid.getMineCount();
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
                if(!interactPressed) {
                    if(interactCounter > 0.25) {
                        currState = State.FLAG;
                        lastState = State.INTERACT;
                    } else {
                        currState = State.REVEAL;
                        lastState = State.INTERACT;
                    }
                    interactCounter = 0;
                }
                break;           
            case REVEAL:
                if(lastState != currState) {
                    lastState = currState;
                    grid.reveal();
                }
            case FLAG:
                if(lastState != currState) {
                    lastState = currState;
                    if(!grid.isSelectedRevealed()){
                        if(grid.flag()) {
                            flagCounter++;
                        } else {
                            flagCounter--;
                        }
                    }
                }
                lastState = State.FLAG;
                break;
            case NONE:
                lastState = State.NONE;
                break;
        }
    }

}