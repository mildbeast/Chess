package com.example.chess;

import java.util.ArrayList;
import java.util.List;

public class PieceSet {
    Player.Color color;
    List<Piece> pieces;

    public PieceSet(Player.Color c) {
        color = c;
        pieces = new ArrayList<Piece>();
    }

    public void init() {
        pieces.clear();

    }

    public void add(Piece piece) {
        pieces.add(piece);
    }

    public Piece get(Piece.Type t) {
        for(Piece p: pieces){
            if(p.type == t)
                return p;
        }
        return null;
    }

    public List<Piece> getLivePieces() {
        List<Piece> livePieces = new ArrayList<Piece>();
        for(Piece p: pieces){
            if(p.live) livePieces.add(p);
        }
        return livePieces;
    }
}
