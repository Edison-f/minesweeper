package com.minesweeper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class UI {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Skin skin;

    private TextInput textInput;
    private TextFieldStyle textFieldStyle;
    private TextField textField;

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

    public enum MenuState {
        OFF,
        ON,
        MINE,
    }

    private State currState;
    private State lastState;

    private MenuState menuState;
    private MenuState lastMenuState;

    Grid grid;

    private float interactCounter;
    private boolean interactPressed;

    private int flagCounter;
    private int mineCount;

    public UI(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, TextInput textInput, Grid grid, int mineCount) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.skin = new Skin();

        this.font = font;

        this.skin.add("default", font);
        
        this.textInput = textInput;
        this.textFieldStyle = new TextFieldStyle();
        this.textFieldStyle.font = skin.getFont("default");
        this.textField = new TextField("", this.textFieldStyle);
        this.textField.setMessageText("10");
        this.textField.setPosition(300, 300);
        

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        this.currState = State.NONE;
        this.menuState = MenuState.OFF;

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

    private void renderResetButton() {
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
    }

    private void renderDPad() {
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
    }

    private void renderMenu() {
        batch.begin();
        textField.setMessageText(textInput.getText());
        textField.draw(batch, 1.0f);
        font.draw(batch, "test: " + menuState, 30, 100);
        font.draw(batch, "textInput: " + textInput.getText(), 30, 120);
        font.draw(batch, "textInput: " + textInput.isOpen(), 30, 140);
        batch.end();
    }

    private void renderResult(boolean won) {

    }

    private void renderInteractButton() {
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
    }

    private void renderMineCounter() {
        // Draw the mine counter
        batch.begin();
        font.draw(batch, "Mines: " + (mineCount - flagCounter), 30, (10 * -1 + windowHeight) / 2);
        batch.end();
    }

    public void render() {
        renderResetButton();
        renderDPad();
        renderInteractButton();
        renderMineCounter();

        if(menuState == MenuState.ON) {
            renderMenu();
        }
        if(true) {
            renderResult(true);
        }

    }

    public void processInputs(int x, int y) {
        // Check if the user clicked on the reset button
        if ((x > resetOffsetX && x < resetOffsetX + windowWidth / 5 && y > resetOffsetY && y < resetOffsetY + windowHeight / 10) || Gdx.input.isKeyPressed(Input.Keys.R)) {
            currState = State.RESET;
            flagCounter = 0;
        } else if(currState != State.INTERACT && currState == lastState) {
            currState = State.NONE;
        }

        // Check if the user clicked on the dpad
        if(x > dPadOffsetX && x < dPadOffsetX + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currState = State.LEFT;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY - windowHeight / 15 && y < dPadOffsetY - windowHeight / 15 + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currState = State.DOWN;
        } else if(x > dPadOffsetX + windowWidth / 8 && x < dPadOffsetX + windowWidth / 8 + dPadWidth && y > dPadOffsetY + windowHeight / 15 && y < dPadOffsetY + windowHeight / 15 + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currState = State.UP;
        } else if(x > dPadOffsetX + windowWidth / 4 && x < dPadOffsetX + windowWidth / 4 + dPadWidth && y > dPadOffsetY && y < dPadOffsetY + dPadHeight || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
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

        if(menuState == MenuState.OFF && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuState = MenuState.ON;
        } else if(menuState == MenuState.ON && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuState = MenuState.OFF;
        }

        if(menuState == MenuState.ON && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            menuState = MenuState.MINE;
        } else if(menuState == MenuState.MINE && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            menuState = MenuState.ON;
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

        switch(menuState) {
            case ON: 
                renderMenu();
                lastMenuState = MenuState.ON;
                break;
            case OFF:
                lastMenuState = MenuState.OFF;
                break;
            case MINE:
                renderMenu();
                if(lastMenuState != menuState) {
                    Gdx.input.getTextInput(textInput, "Number of Mines", textInput.getText(), "Don't put too many");
                }

                lastMenuState = menuState;
                break;
        }
    }

}