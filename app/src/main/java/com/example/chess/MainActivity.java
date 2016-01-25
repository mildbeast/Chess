package com.example.chess;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chess.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements com.example.chess.View{
    private static final String TAG = "MainActivity";
    ViewModel viewModel;
    ImageView[][] viewCells;
    Map<View, Point> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModel(this);
        binding.setViewmodel(viewModel);

        initLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewModel.startNewGame();
    }

    private void initLayout() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int minPixels = Math.min(metrics.widthPixels, metrics.heightPixels);

        GridLayout gl = (GridLayout) findViewById(R.id.board);
        viewCells = new ImageView[Board.SIZE][Board.SIZE];
        map = new HashMap<View, Point>();

        for(int i=0; i < viewCells.length; i++){
            for(int j=0; j < viewCells[0].length; j++){
                viewCells[i][j] = new ImageView(this);
                viewCells[i][j].setBackgroundColor(((i + j) % 2 == 0) ? Color.LTGRAY : Color.GRAY);
                viewCells[i][j].setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "x=" + map.get(v).x + ", y=" + map.get(v).y);
                        viewModel.cellClick(map.get(v));
                    }
                });

                GridLayout.LayoutParams param = new GridLayout.LayoutParams(
                        GridLayout.spec(i, GridLayout.CENTER),
                        GridLayout.spec(j, GridLayout.CENTER)
                );
                param.setGravity(Gravity.FILL);

                param.width = minPixels / (Board.SIZE + 1);
                param.height = minPixels / (Board.SIZE + 1);

                gl.addView(viewCells[i][j], param);
                map.put(viewCells[i][j], new Point(i,j));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start_game) {
            viewModel.startNewGame();
            return true;
        } else if (id == R.id.action_cancel_move) {
            viewModel.cancelMove();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showWinner(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateCell(int x, int y, int drawableID, boolean selected, boolean validMove) {
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{ContextCompat.getDrawable(this, drawableID)});
        ImageView cell = viewCells[x][y];

//        Log.d(TAG, "drawableID = " + drawableID + ", selected = " + selected + ", validMove = " + validMove);

        if (validMove){
            Log.d(TAG, "validMove: x = " + x + ", y = " + y);
            layerDrawable.addLayer(getResources().getDrawable(R.drawable.valid_move_shape, getTheme()));
        }

        if (selected){
            Log.d(TAG, "selected: x = " + x + ", y = " + y);
            cell.setColorFilter(Color.argb(0x80, 0x80, 0, 0));
        }else{
            cell.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        cell.setImageDrawable(layerDrawable);
    }
}
