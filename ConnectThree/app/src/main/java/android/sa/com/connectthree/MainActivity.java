package android.sa.com.connectthree;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    enum Player {
        EMPTY,RED, YELLOW
    }
    final int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    private Player activeColor;
    private boolean ready;
    private Player[] gameState = new Player[9];
    private int rotation;

    public void playAgain(View view) {
        init();
    }

    private void init() {
        for (int i=0;i<gameState.length;i++)
            gameState[i] = Player.EMPTY;
        GridLayout layout = (GridLayout)findViewById(R.id.gridLayout);
        for (int i = 0; i < layout.getChildCount();i++)
            ((ImageView) layout.getChildAt(i)).setImageDrawable(null);
        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
        winnerTextView.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);
        winnerTextView.setVisibility(View.INVISIBLE);
        activeColor = Player.RED;
        rotation = 360;
        ready = true;
    }

    public void dropIn(View view) {

        final Integer index = Integer.valueOf(view.getTag().toString());

        if( ready && Player.EMPTY == gameState[index] ) {
            ImageView selectedImageView = (ImageView) view;
            selectedImageView.setTranslationY(-1500);
            if (activeColor == Player.RED) {
                selectedImageView.setImageDrawable(getDrawable(R.drawable.red));
                gameState[index] = activeColor;
                activeColor = Player.YELLOW;
            } else {
                selectedImageView.setImageResource(R.drawable.yellow);
                gameState[index] = activeColor;
                activeColor = Player.RED;
            }

            rotation = rotation * -1;
            selectedImageView.animate()
                    .translationYBy(1500)
                    .rotation(rotation)
                    .setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
                            TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
                            for (int[] winningPosition : winningPositions) {
                                if(gameState[winningPosition[0]] == gameState[winningPosition[1]]
                                        && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                                        && gameState[winningPosition[2]] != Player.EMPTY) {
                                    ready = false;
                                    winnerTextView.setText(gameState[index].toString() + " has won!");
                                    playAgainButton.setVisibility(View.VISIBLE);
                                    winnerTextView.setVisibility(View.VISIBLE);
                                }
                            }
                            int countEmpty = 0;
                            for (Player p :gameState) {
                                if (p == Player.EMPTY) countEmpty++;
                            }
                            if(countEmpty < 2 && playAgainButton.getVisibility() == View.INVISIBLE){
                                ready = false;
                                winnerTextView.setText("Its a draw!");
                                playAgainButton.setVisibility(View.VISIBLE);
                                winnerTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }).start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
}
