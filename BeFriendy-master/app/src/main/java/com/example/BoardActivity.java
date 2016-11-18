package com.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.EssentialClasses.Game;
import com.example.EssentialClasses.Player;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class BoardActivity extends AppCompatActivity {
    private Game game;

    @BindViews({R.id.field_1, R.id.field_2, R.id.field_3, R.id.field_4,
                R.id.field_5, R.id.field_6, R.id.field_7, R.id.field_8, R.id.field_9,
                R.id.field_10, R.id.field_11, R.id.field_12, R.id.field_13, R.id.field_14,
                R.id.field_15, R.id.field_16, R.id.field_17, R.id.field_18, R.id.field_19,
                R.id.field_20, R.id.field_21, R.id.field_22, R.id.field_23, R.id.field_24,
                R.id.field_25, R.id.field_26, R.id.field_27, R.id.field_28, R.id.field_29}) //TODO CHANGE HERE
    protected List<ImageView> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout_big);
        ButterKnife.bind(this);

        initializeGame();
    }

    private void initializeGame() {
        //TODO CHANGE HERE
        Player player1 = new Player("Sebastian", "sebastian@befirendy.com");
        Player player2 = new Player("Michal", "michal@befirendy.com");
        this.game = new Game(1, player1, player2);
        populateFieldViews();
    }

    private void populateFieldViews() {
        int position = 1;
        for (ImageView field : fields) {
            field.setImageResource(game.getBoard().getFieldByPosition(position).getDrawableId());
            ++position;
        }
    }
}
