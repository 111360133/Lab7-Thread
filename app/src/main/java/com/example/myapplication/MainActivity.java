package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int progressRabbit = 0;
    private int progressTurtle = 0;

    private Button btn_start;
    private SeekBar sb_rabbit, sb_turtle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_start = findViewById(R.id.btnStart);
        sb_rabbit = findViewById(R.id.sbRabbit);
        sb_turtle = findViewById(R.id.sbTurtle);
            btn_start.setOnClickListener(v -> {
                btn_start.setEnabled(false);
                progressRabbit = 0;
                progressTurtle = 0;
                sb_rabbit.setProgress(0);
                sb_turtle.setProgress(0);

                runRabbit();
                runTurtle();
            });
    }
    private final Handler handler = new Handler(Looper.myLooper(), new Handler.Callback(){
       @Override
        public boolean handleMessage(@NonNull Message msg){
           if (msg.what == 1)
               sb_rabbit.setProgress(progressRabbit);
           else if (msg.what == 2)
               sb_turtle.setProgress(progressTurtle);

           if (progressRabbit >= 100 && progressTurtle < 100){
               Toast.makeText(MainActivity.this, "兔子勝", Toast.LENGTH_SHORT).show();
               btn_start.setEnabled(true);
           } else if (progressTurtle >= 100 && progressRabbit < 100){
               Toast.makeText(MainActivity.this, "烏龜勝", Toast.LENGTH_SHORT).show();
               btn_start.setEnabled(true);
           }
           return false;
       }
    });

    private void runRabbit(){
        new Thread(() -> {

            Boolean[] sleepProbability = {true, true, false};

            while(progressRabbit <= 100 && progressTurtle < 100){
                try{
                    Thread.sleep(100);

                    if (sleepProbability[(int) (Math.random() * 3)])
                        Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                progressRabbit += 3;

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void runTurtle(){
        new Thread(() -> {
            while(progressTurtle <= 100 && progressRabbit < 100){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressTurtle += 1;

                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();
    }
}