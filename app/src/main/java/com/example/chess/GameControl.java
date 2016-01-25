package com.example.chess;

import android.graphics.Color;
import android.graphics.Point;

public class GameControl {
    Player curPlayer;
    final Player blackPlayer = new Player(Player.Color.BLACK);
    final Player whitePlayer = new Player(Player.Color.WHITE);

    Board board;
    View view;

    public GameControl(View v, Board b){
        view = v;
        board = b;
    }

    public void onCellSelected(Point p){
        Piece piece = board.getPiece(p);

        if(!board.cellSelected()){
            if(piece != null && piece.color == curPlayer.color){
                board.selectCell(p);
            }
        }else{
            if(board.validMoves().contains(p)){
                finishMove(p);
            }else if(piece == null || piece.color == curPlayer.color){
                board.selectCell(p);
            }
        }
    }

    private void finishMove(Point p) {
        board.makeMove(p);
        Board.Status status = board.getStatus();

        if(isGameEnd(status)){
            endGame();
        }else{
            changeTurn();
        }
    }

    public void endGame() {
        view.showWinner(curPlayer.color.toString() + " player win.");
    }

    public void startNewGame() {
        board.init();
        curPlayer = blackPlayer;
    }

    public int getCurrentPlayerColor(){
        return curPlayer.color == Player.Color.BLACK ? Color.BLACK : Color.WHITE;
    }

    public void changeTurn() {
        curPlayer = (curPlayer == blackPlayer) ? whitePlayer : blackPlayer;
    }

    private boolean isGameEnd(Board.Status status) {
        return status == Board.Status.BLACK_CHECKMATE
                || status == Board.Status.WHITE_CHECKMATE;
    }

    public String getCurrentPlayerText() {
        return curPlayer.color.toString();
    }
}
