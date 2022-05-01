package com.minesweeper.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

import java.util.*;

public class Grid {
    private int width;
    private int height;

    private int windowHeight;
    private int windowWidth;

    private int numMines;

    private ShapeRenderer shapeRenderer;

    private Tile[][] grid;

    public Grid(int width, int height, int numMines, ShapeRenderer shapeRenderer) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        this.shapeRenderer = shapeRenderer;
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
        unsortedArray.toArray(sortedList);

        int index = 0;
        grid = new Tile[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; i < width; i++){
                grid[i][j] = sortedList[index];
                index += 1;
            }
        }

        
    }   
    
    public void render() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(j * windowWidth / width + windowWidth / 15, i * windowHeight / height 
                    + windowHeight / 15, (windowWidth / 20 * (windowWidth - windowWidth / 10)) / width, (windowHeight / 20 * (windowHeight - windowHeight / 10)) / height);
                shapeRenderer.end();
            }
        }
    }
    

}
