package android.sa.com.guessthecelebrity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Celebrity> celebrities;
//    private List<String> options = new ArrayList<>(4);
    private int answer;
    private Button[] buttons = new Button[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            celebrities = new DownloadHtmlTask().execute("http://www.posh24.se/kandisar").get();
        } catch (Exception e) {
            Log.e("init", "unable to get data.",e);
        }
        if(celebrities.size() < 1){
            Toast.makeText(getApplicationContext(),"Can't play now due to network error!",Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
            return;
        }

        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        newQuestion();
    }

    private void newQuestion() {
        Random random = new Random(System.currentTimeMillis());
        int totalCelebrates = celebrities.size();
        int celebId = random.nextInt(totalCelebrates);

        answer = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            int notCurrentCeleb = random.nextInt(totalCelebrates);
            while (notCurrentCeleb == celebId){
                notCurrentCeleb = random.nextInt(totalCelebrates);
            }
            buttons[i].setText(celebrities.get(notCurrentCeleb).getName());
        }
        Celebrity celebrity = celebrities.get(celebId);
        buttons[answer].setText(celebrity.getName());
        Bitmap bitmap = null;
        try {
            bitmap = new DownloadImageTask().execute(celebrity.getImgUrl()).get();
        } catch (Exception e) {
            Log.e("image load", "image not loaded", e);
            newQuestion();
        }
        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
    }

    public void checkAnswer(View view){
        Integer check = Integer.valueOf(view.getTag().toString());
        final ImageView answerImageView = findViewById(R.id.answerImageView);
        final ImageView questionImageView = findViewById(R.id.questionImageView);

        questionImageView.setVisibility(View.INVISIBLE);
        answerImageView.setTranslationY(-1000);
        ViewPropertyAnimator viewPropertyAnimator = answerImageView.animate().translationYBy(1000).setDuration(500);
        viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                answerImageView.setVisibility(View.INVISIBLE);
                questionImageView.setVisibility(View.VISIBLE);
                newQuestion();
            }
        });
        if(check == answer) {
            answerImageView.setImageDrawable(getDrawable(R.drawable.correct));
            answerImageView.setVisibility(View.VISIBLE);
            viewPropertyAnimator.start();
        }else {
            answerImageView.setImageDrawable(getDrawable(R.drawable.incorrect));
            answerImageView.setVisibility(View.VISIBLE);
            viewPropertyAnimator.start();
            String toastTxt = String.format("Correct answer was %s",buttons[answer].getText().toString());
            Toast.makeText(getApplicationContext(),toastTxt,Toast.LENGTH_SHORT).show();
        }
    }
}
