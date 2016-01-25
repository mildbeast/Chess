package com.example.chess;

public interface View {
    void showWinner(String s);

    void updateCell(int x, int y, int drawableID, boolean selected, boolean validMove);
}
