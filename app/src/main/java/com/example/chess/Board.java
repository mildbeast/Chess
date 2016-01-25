package com.example.chess;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 8;

    View view;
    Cell[][] cells;
    Point from;
    List<Move> moveHistory;

    PieceSet blackPieceSet;
    PieceSet whitePieceSet;
    public Status status;

    public Board(View v) {
        view = v;
        cells = new Cell[SIZE][SIZE];
        for(int i=0; i < cells.length; i++){
            for(int j=0; j < cells[0].length; j++){
                cells[i][j] = new Cell();
            }
        }
        moveHistory = new ArrayList<Move>();

        blackPieceSet = new PieceSet(Player.Color.BLACK);
        whitePieceSet = new PieceSet(Player.Color.WHITE);

        status = Status.NONE;
    }


    public Piece getPiece(Point p) {
        return cells[p.x][p.y].piece;
    }

    public void setPiece(Point p, Piece piece) {
        cells[p.x][p.y].piece = piece;
    }

    public void selectCell(Point p) {
        if(from != null)
            cells[from.x][from.y].selected = false;
        if(p != null)
            cells[p.x][p.y].selected = true;
        from = p;
        updateBoard();
    }

    public List<Point> validMoves() {
        return validMoves(from);
    }

    private List<Point> validMoves(Point p) {
        if(p == null || getPiece(p) == null) return new ArrayList<Point>();
        return getPiece(p).validMove();
    }

    public void makeMove(Point to) {
        Move move = new Move(getPiece(from), from,  to, getPiece(to));
        moveHistory.add(move);
        selectCell(null);
        updateStatus(getPiece(to).color);
    }

    public boolean cancelMove() {
        if (moveHistory.isEmpty()) return false;

        Move move = moveHistory.remove(moveHistory.size()-1);
        move.cancel();
        updateBoard();
        return true;
    }

    private void updateBoard() {
        for(int i=0; i < cells.length; i++){
            for(int j=0; j < cells[0].length; j++){
                cells[i][j].validMove = false;
            }
        }
        for(Point p : validMoves()){
            cells[p.x][p.y].validMove = true;
        }
        for(int i=0; i < cells.length; i++){
            for(int j=0; j < cells[0].length; j++){
                view.updateCell(i, j, Piece.getDrawableId(getPiece(new Point(i,j))), cells[i][j].selected, cells[i][j].validMove);
            }
        }
    }

    boolean check(Player.Color color, Point kingPosition){
        for(Piece p : getPieceSet(color).getLivePieces()){
            if(p.validMove().contains(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private void updateStatus(Player.Color color) {
        status = Status.NONE;
        Piece opponentKing = getOpponentPieceSet(color).get(Piece.Type.KING);
        if(check(color, opponentKing.position)){
            status = (color == Player.Color.BLACK) ? Status.WHITE_CHECKMATE : Status.BLACK_CHECKMATE;
            for(Point vm : opponentKing.validMove()){
                if(!check(color, vm)){
                    status = (color == Player.Color.BLACK) ? Status.WHITE_CHECK : Status.BLACK_CHECK;
                    break;
                }
            }
        }
    }

    private PieceSet getPieceSet(Player.Color color) {
        return color == Player.Color.BLACK ? blackPieceSet : whitePieceSet;
    }

    private PieceSet getOpponentPieceSet(Player.Color color) {
        return color == Player.Color.BLACK ? whitePieceSet : blackPieceSet;
    }

    public boolean cellSelected() {
        return from != null;
    }

    public void init() {
        initCell();
        from = null;
        moveHistory.clear();

        blackPieceSet.init();
        whitePieceSet.init();

        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.ROOK, new Point(0,0), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.ROOK, new Point(0,7), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.KNIGHT, new Point(0,1), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.KNIGHT, new Point(0,6), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.BISHOP, new Point(0,2), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.BISHOP, new Point(0,5), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.QUEEN, new Point(0,3), this));
        blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.KING, new Point(0,4), this));
        for(int i=0; i < SIZE; i++)
            blackPieceSet.add(new Piece(Player.Color.BLACK, Piece.Type.PAWN, new Point(1,i), this));

        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.ROOK, new Point(7,0), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.ROOK, new Point(7,7), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.KNIGHT, new Point(7,1), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.KNIGHT, new Point(7,6), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.BISHOP, new Point(7,2), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.BISHOP, new Point(7,5), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.QUEEN, new Point(7,3), this));
        whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.KING, new Point(7,4), this));
        for(int i=0; i < SIZE; i++)
            whitePieceSet.add(new Piece(Player.Color.WHITE, Piece.Type.PAWN, new Point(6,i), this));

        updateBoard();
    }

    private void initCell() {
        for(int i=0; i < cells.length; i++){
            for(int j=0; j < cells[0].length; j++){
                cells[i][j].piece = null;
                cells[i][j].selected = false;
                cells[i][j].validMove = false;
            }
        }
    }

    enum Status{
        NONE("None"),
        BLACK_CHECK("Black Check"),
        WHITE_CHECK("White Check"),
        BLACK_CHECKMATE("Black Checkmate"),
        WHITE_CHECKMATE("White Checkmate");

        String str;
        Status(String s) {
            str = s;
        }

        public String toString(){
            return str;
        }
    }

    public Status getStatus() {
        return status;
    }

    private class Cell {
        public Piece piece;
        public boolean selected;
        public boolean validMove;
    }
}
