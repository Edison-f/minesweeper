package com.minesweeper.game;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInput implements TextInputListener {
    
    private String text;
    private boolean open;

    @Override
    public void input (String text) {
        open = true;
        System.out.println("EIGYeiusobjewruwyhei ");
        this.text = text;
    }
 
    @Override
    public void canceled () {
        open = false;
        System.out.println("oirwbunipoejw[gpihbivjvwhkboip");
    }

    public String getText() {
        return text;
    }

    public boolean isOpen() {
        return open;
    }
}
