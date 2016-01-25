package com.example.chess;

import android.graphics.Point;

public class Move {
    Piece piece;
    Piece captured;

    Point from;
    Point to;

    public Move(Piece p, Point f, Point t, Piece c) {
        piece = p;
        from = f;
        to = t;
        captured = c;
        if (captured != null) captured.live = false;
        piece.move(to);
    }

    public void cancel() {
        piece.move(from);
        if (captured != null) {
            captured.live = true;
            captured.move(to);
        }
    }
}

