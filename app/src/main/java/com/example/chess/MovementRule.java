package com.example.chess;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MovementRule {
    public int distance;
    public Direction dir;
    public boolean captureOnly;
    public boolean moveOnly;

    public static List<MovementRule> create(Piece piece) {
        if(piece == null) return null;

        List<MovementRule> rules = new ArrayList<MovementRule>();
        switch(piece.type){
            case PAWN:
                if(piece.color == Player.Color.WHITE){
                    rules.add(new MovementRule(1, Direction.DOWN, false, true));
                    rules.add(new MovementRule(1, Direction.DOWN_LEFT, true, false));
                    rules.add(new MovementRule(1, Direction.DOWN_RIGHT, true, false));
                }else{
                    rules.add(new MovementRule(1, Direction.UP, false, true));
                    rules.add(new MovementRule(1, Direction.UP_LEFT, true, false));
                    rules.add(new MovementRule(1, Direction.UP_RIGHT, true, false));
                }
                break;
            case BISHOP:
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP_LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN_LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP_RIGHT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN_RIGHT));
                break;
            case KNIGHT:
                rules.add(new MovementRule(1, Direction.KNIGHT_UP_1));
                rules.add(new MovementRule(1, Direction.KNIGHT_UP_2));
                rules.add(new MovementRule(1, Direction.KNIGHT_DOWN_1));
                rules.add(new MovementRule(1, Direction.KNIGHT_DOWN_2));
                rules.add(new MovementRule(1, Direction.KNIGHT_LEFT_1));
                rules.add(new MovementRule(1, Direction.KNIGHT_LEFT_2));
                rules.add(new MovementRule(1, Direction.KNIGHT_RIGHT_1));
                rules.add(new MovementRule(1, Direction.KNIGHT_RIGHT_2));
                break;
            case ROOK:
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN));
                rules.add(new MovementRule(Board.SIZE-1, Direction.LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.RIGHT));
                break;
            case QUEEN:
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN));
                rules.add(new MovementRule(Board.SIZE-1, Direction.LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.RIGHT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP_LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN_LEFT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.UP_RIGHT));
                rules.add(new MovementRule(Board.SIZE-1, Direction.DOWN_RIGHT));
                break;
            case KING:
                rules.add(new MovementRule(1, Direction.UP));
                rules.add(new MovementRule(1, Direction.DOWN));
                rules.add(new MovementRule(1, Direction.LEFT));
                rules.add(new MovementRule(1, Direction.RIGHT));
                rules.add(new MovementRule(1, Direction.UP_LEFT));
                rules.add(new MovementRule(1, Direction.DOWN_LEFT));
                rules.add(new MovementRule(1, Direction.UP_RIGHT));
                rules.add(new MovementRule(1, Direction.DOWN_RIGHT));
                break;
        }
        return rules;
    }

    enum Direction{
        UP(1, 0),
        DOWN(-1, 0),
        LEFT(0, -1),
        RIGHT(0, 1),
        UP_LEFT(1, -1),
        DOWN_LEFT(-1, -1),
        UP_RIGHT(1, 1),
        DOWN_RIGHT(-1, 1),

        KNIGHT_UP_1(2, -1),
        KNIGHT_UP_2(2, 1),
        KNIGHT_DOWN_1(-2, -1),
        KNIGHT_DOWN_2(-2, 1),
        KNIGHT_LEFT_1(1, -2),
        KNIGHT_LEFT_2(-1, -2),
        KNIGHT_RIGHT_1(1, 2),
        KNIGHT_RIGHT_2(-1, 2);

        public int dx, dy;

        Direction(int x, int y) {
            dx = x; dy = y;
        }
    }

    public MovementRule(int dis, Direction d) {
        this(dis, d, false, false);
    }

    public MovementRule(int dis, Direction d, boolean cOnly, boolean mOnly){
        distance = dis;
        dir = d;
        captureOnly = cOnly;
        moveOnly = mOnly;
    }

    public List<Point> getPointsByRule(Point p){
        List<Point> points = new ArrayList<Point>();
        for(int i=1; i <= distance; i++){
            int nx = p.x + i * dir.dx;
            int ny = p.y + i * dir.dy;
            if(valid(nx) && valid(ny))
                points.add(new Point(nx, ny));
        }
        return points;
    }

    private boolean valid(int n) {
        return (n >= 0 && n < Board.SIZE);
    }
}
