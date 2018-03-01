package com.gjf.pedometer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {

    private TextView tvStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        tvStep = findViewById(R.id.tv_step);

        register();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void register() {
        StepCounterManager.getInstance().addStepCounterObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                tvStep.setText("芯片实时获取步数: " + (float) arg );
            }
        });

        StepCounterManager.getInstance().register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        StepCounterManager.getInstance().clearStepObserver();
        StepCounterManager.getInstance().unRegister();
    }
}
