package com.example.SysLab32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnCount;
    private EditText inputP, editP1x1, editP1x2, editP2x1, editP2x2;
    private RadioGroup learning;
    private RadioGroup deadline;
    private RadioGroup iters;
    private double speed = 0.001, time = 0.5, p, p1x1, p1x2, p2x1, p2x2;
    private int iterations = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        learning = findViewById(R.id.learning);
        learning.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.speed0_001:
                        speed = 0.001;
                        break;
                    case R.id.speed0_01:
                        speed = 0.01;
                        break;
                    case R.id.speed0_05:
                        speed = 0.05;
                        break;
                    case R.id.speed0_1:
                        speed = 0.1;
                        break;
                    case R.id.speed0_2:
                        speed = 0.2;
                        break;
                    case R.id.speed0_3:
                        speed = 0.3;
                        break;
                    default:
                        break;
                }
            }
        });

        deadline = findViewById(R.id.deadline);
        deadline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.time0_5:
                        time = 0.05;
                        break;
                    case R.id.time1:
                        time = 1.0;
                        break;
                    case R.id.time2:
                        time = 2.0;
                        break;
                    case R.id.time5:
                        time = 5.0;
                        break;
                    default:
                        break;
                }
            }
        });

        iters = findViewById(R.id.iterations);
        iters.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.iterations100:
                        iterations = 100;
                        break;
                    case R.id.iterations200:
                        iterations = 200;
                        break;
                    case R.id.iterations500:
                        iterations = 500;
                        break;
                    case R.id.iterations1000:
                        iterations = 1000;
                        break;
                    default:
                        break;
                }
            }
        });

        inputP = findViewById(R.id.p_input);
        inputP.setText("4");
        editP1x1 = findViewById(R.id.editP1x1);
        editP1x1.setText("0");
        editP1x2 = findViewById(R.id.editP1x2);
        editP1x2.setText("6");
        editP2x1 = findViewById(R.id.editP2x1);
        editP2x1.setText("1");
        editP2x2 = findViewById(R.id.editP2x2);
        editP2x2.setText("5");

        final TextView res = findViewById(R.id.res_txt);

        btnCount = findViewById(R.id.count_btn);
        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = new Double(inputP.getText().toString());
                p1x1 = new Double(editP1x1.getText().toString());
                p1x2 = new Double(editP1x2.getText().toString());
                p2x1 = new Double(editP2x1.getText().toString());
                p2x2 = new Double(editP2x2.getText().toString());

                Point point1 = new Point(p1x1, p1x2);
                Point point2 = new Point(p2x1, p2x2);
                res.setText(neural(point1, point2, p, iterations, time, speed));
            }
        });
    }

    private class Point {
        public double x1;
        public double x2;

        Point(double x, double y) {
            this.x1 = x;
            this.x2 = y;
        }
    }

    private String neural (Point p1, Point p2, double p, int iterations, double time, double speed) {
        String resultOutputString = "";
        double startTime = System.currentTimeMillis();

        double w1 = 0.0, w2 = 0.0;
        double y1 = p1.x1 * w1 + p1.x2 * w2;
        double y2 = p2.x1 * w1 + p2.x2 * w2;
        int iteration = 0;
        Point point;
        boolean isPoint1 = true;
        while(y1 >= p || y2 <= p) {
            point = isPoint1 ? p1 : p2;
            double delta = isPoint1 ? (p - y1) : (p - y2);
            w1 += delta * speed * point.x1;
            w2 += delta * speed * point.x2;

            y1 = p1.x1 * w1 + p1.x2 * w2;
            y2 = p2.x1 * w1 + p2.x2 * w2;

            iteration++;
            isPoint1 = !isPoint1;

            if (iteration >= iterations) {
                resultOutputString = "Too many iterations!\n";
                break;
            }

            double currentTime = System.currentTimeMillis();
            if (currentTime - startTime >= time * 1000) {
                resultOutputString = "Reached deadline time!\n";
                break;
            }
        }
        Toast iterationsMessage = Toast.makeText(MainActivity.this, "Iterations:" + Integer.toString(iteration), Toast.LENGTH_LONG);
        iterationsMessage.show();
        resultOutputString += "w1 = " + ((double) Math.round(w1 * 100) / 100) + "; w2 = " + ((double) Math.round(w2 * 100) / 100);
        return resultOutputString;
    }
}
