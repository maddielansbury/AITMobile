package hu.ait.minesweeper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hu.ait.minesweeper.view.MinesweeperView;

import static hu.ait.minesweeper.R.id.minefield;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MinesweeperView minefield = (MinesweeperView) findViewById(R.id.minefield);

        Button btnRetry = (Button) findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minefield.newGame();
            }
        });

        Button btnMode = (Button) findViewById(R.id.btnMode);
        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minefield.toggleMode();
            }
        });

    }

    public void setModeMsg(String text) {
        ((TextView) findViewById(R.id.modeText)).setText(text);
    }

}
