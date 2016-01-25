package com.example.chess;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;

public class ViewModel extends BaseObservable {
    Board board;
    GameControl gc;
    View view;

    public ViewModel(View v){
        view = v;
        board = new Board(v);
        gc = new GameControl(v, board);
    }

    public void startNewGame() {
        gc.startNewGame();
    }

    public void endGame() {
        gc.endGame();
    }

    public void cancelMove() {
        if(board.cancelMove()){
            gc.changeTurn();
            notifyChange();
        }
    }

    public void cellClick(Point p) {
        gc.onCellSelected(p);
        notifyChange();
    }

    @Bindable
    public String getStatus(){
        return "Status: " + board.getStatus();
    }

    @Bindable
    public int getTurnColor(){
        return gc.getCurrentPlayerColor();
    }

    @Bindable
    public String getTurnText(){
        return gc.getCurrentPlayerText() + " turn";
    }
}
