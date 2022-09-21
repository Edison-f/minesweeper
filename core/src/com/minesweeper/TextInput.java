package com.minesweeper;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInput implements TextInputListener {
    
    private String text;
    private boolean open;

    @Override
    public void input (String text) {
        open = true;
        this.text = text;
    }
 
    @Override
    public void canceled () {
        open = false;
    }
}
