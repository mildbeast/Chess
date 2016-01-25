package com.example.chess;

public class Player {
    Color color;
    public Player(Color c) {
        color = c;
    }

    enum Color{
        BLACK("Black"),
        WHITE("White");

        Color(String s) {
        }
    }
}
