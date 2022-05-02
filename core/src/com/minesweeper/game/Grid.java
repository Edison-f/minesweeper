package com.minesweeper.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class Grid {
    private int width;
    private int height;

    private int windowHeight;
    private int windowWidth;

    private int tileSize;
    private int heightOffset;
    private int widthOffset;

    private int numMines;

    private ShapeRenderer shapeRenderer;

    private Tile[][] grid;

    private BitmapFont font;
    private SpriteBatch batch;

    public Grid(int width, int height, int numMines, ShapeRenderer shapeRenderer) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        tileSize = Math.min((((windowWidth - windowWidth / 8) - windowWidth / 40) / width), 
            (((windowHeight - windowHeight / 8) - windowHeight / 40) / height));

        heightOffset = (windowHeight - (tileSize * height)) / 2;
        widthOffset = (windowWidth - (tileSize * width)) / 2;

        
        this.shapeRenderer = shapeRenderer;

        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    /**
     * Generates a 2d array with a given width and height.
     */
    public void generateGrid() {
        Tile[] unsortedList =  new Tile[width * height];
        Tile[] sortedList =  new Tile[width * height];

        for(int i = 0; i < height * width; i++) {
            if(i < numMines) {
                unsortedList[i] = new Tile(true);
            } else {
                unsortedList[i] = new Tile(false);
            }
        }
        
        List<Tile> unsortedArray = Arrays.asList(unsortedList);
        Collections.shuffle(unsortedArray);
        unsortedArray.toArray(  sortedList);

        int index = 0;
        grid = new Tile[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                grid[i][j] = sortedList[index];
                index += 1;
            }
        }
    }
    
    public void render() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                grid[i][j].render(j * tileSize + widthOffset, i * tileSize + heightOffset, tileSize, shapeRenderer);
            }
        }
    }
    
    public void touchDetection(int x, int y) {
        int xIndex = (x - widthOffset) / tileSize;
        int yIndex = (((-1 * y) - heightOffset + windowHeight)) / tileSize;
        if(xIndex >= 0 && xIndex < width && yIndex >= 0 && yIndex < height) {
            grid[yIndex][xIndex].tapped();
        }
    }
}
