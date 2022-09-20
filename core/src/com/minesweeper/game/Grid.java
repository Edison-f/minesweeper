package com.minesweeper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private BitmapFont font;
    private SpriteBatch batch;

    private Tile[][] grid;

    private int[] cursorLocation;

    public Grid(int width, int height, int numMines, ShapeRenderer shapeRenderer, BitmapFont font, SpriteBatch batch) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;

        this.windowHeight = Gdx.graphics.getHeight();
        this.windowWidth = Gdx.graphics.getWidth();

        tileSize = Math.min((((windowWidth - windowWidth / 8) - windowWidth / 40) / width), 
            (((windowHeight - windowHeight / 4) - windowHeight / 20) / height));

        heightOffset = (windowHeight - (tileSize * height)) / 2 + windowHeight / 14;
        widthOffset = (windowWidth - (tileSize * width)) / 2;

        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.batch = batch;

        cursorLocation = new int[] {0, 0};

        Gdx.gl.glLineWidth(4);
    }

    /**
     * Generates a 2d array with a given width and height.
     */
    public void generateGrid() {
        Tile[] unsortedList =  new Tile[width * height];
        Tile[] sortedList =  new Tile[width * height];

        for(int i = 0; i < height * width; i++) {
            if(i < numMines) {
                unsortedList[i] = new Tile(true, font, batch);
            } else {
                unsortedList[i] = new Tile(false, font, batch);
            }
        }
        
        List<Tile> unsortedArray = Arrays.asList(unsortedList);
        Collections.shuffle(unsortedArray);
        unsortedArray.toArray(sortedList);

        int index = 0;
        grid = new Tile[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                grid[i][j] = sortedList[index];
                index += 1;
            }
        }
    }
    
    public void resetGrid() {
        generateGrid();
        generateAdjacentMines();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                grid[i][j].setState(Tile.State.HIDDEN);
            }
        }

        if(grid[cursorLocation[0]][cursorLocation[1]].isMine() || (grid[cursorLocation[0]][cursorLocation[1]].getAdjacentMines() != 0)) {
            resetGrid();
            reveal();
//            System.out.println(grid[cursorLocation[0]][cursorLocation[1]].getAdjacentMines());
        }
    }

    public void render() {
        for(int i = 0; i < height * 2; i++) {
            for(int j = 0; j < width; j++) {
                if(i < height) {
                    grid[i][j].renderTile(j * tileSize + widthOffset, i * tileSize + heightOffset, tileSize, shapeRenderer, false);
                    grid[i][j].renderNumbers(j * tileSize + widthOffset + tileSize / 2, windowHeight - (i * tileSize + heightOffset + tileSize / 2));                    
                }
            }
        }
        grid[cursorLocation[0]][cursorLocation[1]].renderTile(cursorLocation[1] * tileSize + widthOffset, cursorLocation[0] * tileSize + heightOffset, tileSize, shapeRenderer, true);
        grid[cursorLocation[0]][cursorLocation[1]].renderNumbers(cursorLocation[1] * tileSize + widthOffset + tileSize / 2, windowHeight - (cursorLocation[0] * tileSize + heightOffset + tileSize / 2));                    
    }
    
    public void touchDetection(int x, int y) {
        int xIndex = (x - widthOffset) / tileSize;
        int yIndex = (((-1 * y) - heightOffset + windowHeight)) / tileSize;
        if(((-1 * y) - heightOffset + windowHeight) > 0) {   
            if(xIndex >= 0 && xIndex < width && yIndex >= 0 && yIndex < height) {
                grid[yIndex][xIndex].tapped();
                if(grid[yIndex][xIndex].getAdjacentMines() == 0) {
                    autoReveal(xIndex, yIndex);
                }
            }
        }
    }

    public void autoReveal(int x, int y) {
        if(y > 0) {
            if(x > 0) {
                if(!grid[y - 1][x - 1].isMine() && grid[y - 1][x - 1].isRevealed() == false) {
                    grid[y - 1][x - 1].reveal();
                    if(grid[y - 1][x - 1].getAdjacentMines() == 0 ){
                        autoReveal(x - 1, y - 1);
                    }
                }
            }
            if(!grid[y - 1][x].isMine() && grid[y - 1][x].isRevealed() == false) {
                grid[y - 1][x].reveal();
                if(grid[y - 1][x].getAdjacentMines() == 0 ){
                    autoReveal(x, y - 1);
                }
            }
            if(x < width - 1) {
                if(!grid[y - 1][x + 1].isMine() && grid[y - 1][x + 1].isRevealed() == false) {
                    grid[y - 1][x + 1].reveal();
                    if(grid[y - 1][x + 1].getAdjacentMines() == 0) {
                        autoReveal(x + 1, y - 1);
                    }
                }
            } 
        }
        if(x > 0) {
            if(y < height - 1) {
                if(!grid[y + 1][x - 1].isMine() && grid[y + 1][x - 1].isRevealed() == false) {
                    grid[y + 1][x - 1].reveal();
                    if(grid[y + 1][x - 1].getAdjacentMines() == 0) {
                        autoReveal(x - 1, y + 1);
                    }
                }
            }
            if(!grid[y][x - 1].isMine() && !grid[y][x - 1].isRevealed()) {
                grid[y][x - 1].reveal();
                if(grid[y][x - 1].getAdjacentMines() == 0) {
                    autoReveal(x - 1, y);
                }
            }
        }
        if(y < height - 1) {
            if(x < width - 1) {
                if(!grid[y + 1][x + 1].isMine() && grid[y + 1][x + 1].isRevealed() == false) {
                    grid[y + 1][x + 1].reveal();
                    if(grid[y + 1][x + 1].getAdjacentMines() == 0) {
                        autoReveal(x + 1, y + 1);
                    }
                }
            }
            if(!grid[y + 1][x].isMine() && grid[y + 1][x].isRevealed() == false) {
                grid[y + 1][x].reveal();
                if(grid[y + 1][x].getAdjacentMines() == 0) {
                    autoReveal(x, y + 1);
                }
            }
        }
        if(x < width - 1) {
            if(!grid[y][x + 1].isMine() && grid[y][x + 1].isRevealed() == false) {
                grid[y][x + 1].reveal();
                if(grid[y][x + 1].getAdjacentMines() == 0) {
                    autoReveal(x + 1, y);
                }
            }
        }
    }

    public void generateAdjacentMines() {
        int mineCount;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                mineCount = 0;
                if(i != 0) {
                    if(j != 0) {
                        if(grid[i - 1][j - 1].isMine()) {
                            mineCount += 1;
                        }
                    }
                    if(grid[i - 1][j].isMine()) {
                        mineCount += 1;
                    }
                    if(j != width - 1) {
                        if(grid[i - 1][j + 1].isMine()) {
                            mineCount += 1;
                        }
                    }
                }
                if(j != 0) {
                    if(i != height - 1) {
                        if(grid[i + 1][j - 1].isMine()) {
                            mineCount += 1;
                        }
                    }
                    if(grid[i][j - 1].isMine()) {
                        mineCount += 1;
                    }
                }
                if(i != height - 1) {
                    if(j != width - 1) {
                        if(grid[i + 1][j + 1].isMine()) {
                            mineCount += 1;
                        }
                    }
                    if(grid[i + 1][j].isMine()) {
                        mineCount += 1;
                    }
                }
                if(j != width - 1) {
                    if(grid[i][j + 1].isMine()) {
                        mineCount += 1;
                    }
                }
                grid[i][j].setAdjacentMines(mineCount);
            }
        }
     }

     public void moveUp() {
        if(cursorLocation[0] < height - 1) {
            cursorLocation[0] += 1;
        }
     }

    public void moveDown() {
        if(cursorLocation[0] > 0) {
            cursorLocation[0] -= 1;
        }
    }

    public void moveLeft() {
        if(cursorLocation[1] > 0) {
            cursorLocation[1] -= 1;
        }
    }

    public void moveRight() {
        if(cursorLocation[1] < width - 1) {
            cursorLocation[1] += 1;
        }
    }

    public void reveal() {
//        System.out.println("\nRevealing");
        grid[cursorLocation[0]][cursorLocation[1]].tapped();
        if(grid[cursorLocation[0]][cursorLocation[1]].getAdjacentMines() == 0 && grid[cursorLocation[0]][cursorLocation[1]].isFlagged() == false) {
            autoReveal(cursorLocation[1], cursorLocation[0]);
//            System.out.println("Zero Reveal");
        } else if(!grid[cursorLocation[0]][cursorLocation[1]].isMine() && nonZeroReqMet(cursorLocation[1], cursorLocation[0])) {
            autoReveal(cursorLocation[1], cursorLocation[0]);
//            System.out.println("Flagged Reveal");
        } else {
//            System.out.println("Normal Reveal");
//            System.out.println(nonZeroReqMet(cursorLocation[1], cursorLocation[0]));
        }
    }

    public boolean nonZeroReqMet(int x, int y) {
        int adjacentFlagged = 0;
        if(y > 0) {
            if(x > 0) {
                if(grid[y - 1][x - 1].isFlagged() && grid[y - 1][x - 1].isMine()) {
                    adjacentFlagged += 1;
                    System.out.println("down left");
                }
            }
            if(grid[y - 1][x].isFlagged() && grid[y - 1][x].isMine()) {
                adjacentFlagged += 1;
                System.out.println("down");
            }
            if(x < width - 1) {
                if(grid[y - 1][x + 1].isFlagged() && grid[y - 1][x + 1].isMine()) {
                    adjacentFlagged += 1;
                    System.out.println("down right");
                }
            } 
        }
        if(x > 0) {
            if(y < height - 1) {
                if(grid[y + 1][x - 1].isFlagged() && grid[y + 1][x - 1].isMine()) {
                    adjacentFlagged += 1;
                    System.out.println("up left");
                }
            }
            if(grid[y][x - 1].isFlagged() && grid[y][x - 1].isMine()) {
                adjacentFlagged += 1;
                System.out.println("left");
            }
        }
        if(y < height - 1) {
            if(x < width - 1) {
                if(grid[y + 1][x + 1].isFlagged() && grid[y + 1][x + 1].isMine()) {
                    adjacentFlagged += 1;
                    System.out.println("up right");
                }
            }
            if(grid[y + 1][x].isFlagged() && grid[y + 1][x].isMine()) {
                adjacentFlagged += 1;
                System.out.println("up");
            }
        }
        if(x < width - 1) {
            if(grid[y][x + 1].isFlagged() && grid[y][x + 1].isMine()) {
                adjacentFlagged += 1;
                System.out.println("right");
            }
        }
//        System.out.println("adj flag " + adjacentFlagged);
        return (adjacentFlagged == grid[y][x].getAdjacentMines());
    }


    public boolean flag() {
        return grid[cursorLocation[0]][cursorLocation[1]].flag();
    }

    public boolean isSelectedRevealed() {
        return grid[cursorLocation[0]][cursorLocation[1]].isRevealed();
    }

    public int getMineCount() {
        return numMines;
    }
}
