package android.sa.com.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final private int MAX_TIME = 30 * 1000;
    final private int TICK_INTERVAL = 1000;

    private ArrayList<Button> buttons = new ArrayList<>(4);
    private int question1, question2, score, totalQuestions, answerId;
    private int timer = 30;
    CountDownTimer countDownTimer;

    public void checkAnswer(View view) {
        Integer selection = Integer.valueOf(view.getTag().toString());
        if (selection == answerId){
            score ++;
            totalQuestions++;
            Log.i("check", "correct");
        } else {
            totalQuestions++;
            Log.i("check", "incorrect");
        }
        ((TextView)findViewById(R.id.scoreTextView)).setText(String.format("%d/%d",score,totalQuestions));
        buildQuestion();
    }

    private void buildQuestion(){
        Random random = new Random(System.currentTimeMillis());
        question1 = random.nextInt(100);
        question2 = random.nextInt(100);
        answerId  = random.nextInt(4);
        for ( Button button: buttons) {
            button.setText(String.valueOf(random.nextInt(100)));
        }
        buttons.get(answerId).setText(String.valueOf(question1+question2));
        ((TextView)findViewById(R.id.questionTextView)).setText(String.format("%d+%d=?",question1,question2));
    }

    public void startGame(View view) {
        init();
        findViewById(R.id.goButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.gridLayout).setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer( MAX_TIME , TICK_INTERVAL ) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer--;
                if(timer >= 0) {
                    ((TextView)findViewById(R.id.timertextView)).setText(String.format("%ds",timer));
                }
            }
            @Override
            public void onFinish() {
                ((TextView)findViewById(R.id.questionTextView)).setText("-");
                findViewById(R.id.goButton).setVisibility(View.VISIBLE);
                findViewById(R.id.gridLayout).setVisibility(View.INVISIBLE);
                showToast();
            }
        };
        buildQuestion();
        countDownTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for( int i = 0; i < gridLayout.getChildCount(); i++){
            buttons.add((Button)gridLayout.getChildAt(i));
        }
        init();
    }

    private void init() {
        timer = 30;
        totalQuestions=1;
        score=0;
        ((TextView)findViewById(R.id.scoreTextView)).setText("0/0");
        ((TextView)findViewById(R.id.questionTextView)).setText("-");
        ((TextView)findViewById(R.id.timertextView)).setText("30s");
        for (Button i : buttons) {
            i.setText(String.valueOf(0));
        }
        ((Button)findViewById(R.id.goButton)).setVisibility(View.VISIBLE);
        findViewById(R.id.gridLayout).setVisibility(View.INVISIBLE);
    }

    private void showToast(){
        String msg = String.format("You answered %d correctly out of %d questions.", score,totalQuestions);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
