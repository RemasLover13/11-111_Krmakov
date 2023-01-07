package org.example.main;

import org.example.display.Display;
import org.example.game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import javax.swing.AbstractAction;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();
        game.start();


    }
}