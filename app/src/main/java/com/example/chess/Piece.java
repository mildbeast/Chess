package com.example.chess;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Piece {

    enum Type{
        PAWN,
        BISHOP,
        KNIGHT,
        ROOK,
        QUEEN,
        KING
    }

    public final Player.Color color;
    public final Piece.Type type;

    List<MovementRule> movementRules;
    Board board;

    boolean live;
    Point position;

    public Piece(Player.Color c, Type t, Point p, Board b){
        color = c;
        type = t;
        position = p;
        board = b;
        movementRules = MovementRule.create(this);
        live = true;
        board.setPiece(position, this);
    }

    public static int getDrawableId(Piece piece) {
        if (piece == null) return R.drawable.empty;

        Player.Color c = piece.color;
        Type t = piece.type;

        switch(t){
            case PAWN:
                return (c == Player.Color.BLACK) ? R.drawable.black_pawn : R.drawable.white_pawn;
            case BISHOP:
                return (c == Player.Color.BLACK) ? R.drawable.black_bishop : R.drawable.white_bishop;
            case KNIGHT:
                return (c == Player.Color.BLACK) ? R.drawable.black_knight : R.drawable.white_knight;
            case ROOK:
                return (c == Player.Color.BLACK) ? R.drawable.black_rook : R.drawable.white_rook;
            case QUEEN:
                return (c == Player.Color.BLACK) ? R.drawable.black_queen : R.drawable.white_queen;
            case KING:
                return (c == Player.Color.BLACK) ? R.drawable.black_king : R.drawable.white_king;
        }

        return R.drawable.empty;
    }

    public void move(Point p){
        board.setPiece(position, null);
        board.setPiece(p, this);
        position = p;
    }

    public List<Point> validMove() {
        List<Point> points = new ArrayList<Point>();
        for(MovementRule rule : movementRules){
            List<Point> candidate = rule.getPointsByRule(position);
            for(Point point : candidate){
                Piece pieceOnMove = board.getPiece(point);
                if(pieceOnMove != null){
                    if(pieceOnMove.color != color){
                        if(!rule.moveOnly)
                            points.add(point);
                    }
                    break;
                }else{
                    if(!rule.captureOnly)
                        points.add(point);
                }
            }
        }
        return points;
    }

}
