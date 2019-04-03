package android.sa.com.eggtimer;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int count = 1;
    int countDownStart = 30;
    int MAX_TIME = 60 - 1;
    int INIT_TIME = 30;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timerTextView = findViewById(R.id.timerTextView);
        SeekBar timerSeekBar = findViewById(R.id.timerSeekBar);
        init();

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timerTextView.setText(buildTimerText(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void init(){
        Button startButton = findViewById(R.id.startButton);
        final SeekBar timerSeekBar = findViewById(R.id.timerSeekBar);
        startButton.setText("Start");
        timerSeekBar.setMax(MAX_TIME);
        timerSeekBar.setProgress(INIT_TIME);
        final TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(buildTimerText(timerSeekBar.getProgress()));
        timerSeekBar.setEnabled(true);
    }
    public void startButton(final View view) {
        Button startButton =  ((Button) view);
        final SeekBar timerSeekBar = findViewById(R.id.timerSeekBar);
        int timeToRun = timerSeekBar.getProgress();
        if(startButton.getText().equals("Start")) {
            timerSeekBar.setEnabled(false);
            countDownStart = timeToRun;
            count = 1;
            countDownTimer = buildCountDownTimer(timeToRun);
            countDownTimer.start();
            startButton.setText("Stop");
        } else {
            countDownTimer.cancel();
            init();
        }
    }

    private CountDownTimer buildCountDownTimer(final long timeToRun) {
        return new CountDownTimer(timeToRun * 1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        final TextView timerTextView = findViewById(R.id.timerTextView);
                        timerTextView.setText(buildTimerText(countDownStart-count));
                    }

                    @Override
                    public void onFinish() {
                        final TextView timerTextView = findViewById(R.id.timerTextView);
                        timerTextView.setText("Done!");
                        init();
                    }
                };
    }

    private String buildTimerText(int time) {
        String result="";
        if(time == 0){
            result = "0:00";
        }
        int mins = time/60;
        int secsRemain = time - mins*60;
        secsRemain = secsRemain < 0 ? 0:secsRemain;
        result = String.format("%d:%02d",mins,secsRemain);
        return result;
    }

}
