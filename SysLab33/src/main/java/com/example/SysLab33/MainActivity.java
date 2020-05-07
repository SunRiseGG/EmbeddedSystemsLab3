package com.example.SysLab33;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editA = findViewById(R.id.edit_a);
        final EditText editB = findViewById(R.id.edit_b);
        final EditText editC = findViewById(R.id.edit_c);
        final EditText editD = findViewById(R.id.edit_d);
        final EditText editY = findViewById(R.id.edit_y);
        final TextView resultOutput = findViewById(R.id.text_res);

        Button btnCount = findViewById(R.id.count_btn);
        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a = new Double(editA.getText().toString());
                double b = new Double(editB.getText().toString());
                double c = new Double(editC.getText().toString());
                double d = new Double(editD.getText().toString());
                double y = new Double(editY.getText().toString());
                double mutation = 0.2;
                Genetic geneticAlgorithmInstance = new Genetic();
                for(int i = 0; i < 10; i++) {
                  resultOutput.append(geneticAlgorithmInstance.count(a, b, c, d, y, mutation));
                  mutation += 0.2;
                }
            }
        });
    }

    private class Genetic {
        private int maxIteration = 10000;
        private double mutation = 0;
        private int prevIteration = maxIteration;
        private int currIteration = 0;

        private String count(double a, double b, double c, double d, double y, double mut) {
            mutation = mut;
            maxIteration = (int) Math.round(maxIteration * mut);
            Random randomGenerator = new Random();
            ArrayList<ArrayList<Double>> population = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                population.add(new ArrayList<Double>());
                for (int j = 0; j < 4; j++) {
                    population.get(i).add(randomGenerator.nextDouble() * y / 2);
                }
            }

            int it = 0;
            for (; it < maxIteration; it++) {
                Double[] values = new Double[4];
                Double percent = 0.0;
                for (int i = 0; i < population.size(); i++) {
                    values[i] = countY(population.get(i), a, b, c, d);
                    double delta = values[i] - y;
                    currIteration = it;
                    if (delta == 0.0) {
                        return formatResult(population.get(i));
                    }
                    percent = percent + 1 / values[i];
                }

                Double[] range = new Double[5];
                range[0] = 0.0;
                range[4] = 100.0;
                for (int i = 0; i < range.length - 1; i++) {
                    range[i + 1] = range[i] + (values[i] / percent);
                }

                Integer[] parentsId = new Integer[4];
                for (int i = 0; i < parentsId.length; i++) {
                    int temp = randomGenerator.nextInt(100);
                    int id = 1;
                    while (id < range.length && range[id] < temp) {
                        id++;
                    }
                    parentsId[i] = (id == 0) ? id : id - 1;
                }

                ArrayList<ArrayList<Double>> children = new ArrayList<>();
                for (int i = 0; i < population.size(); i++) {
                    Integer p1 = parentsId[randomGenerator.nextInt(parentsId.length - 1)];
                    Integer p2 = parentsId[randomGenerator.nextInt(parentsId.length - 1)];
                    int threshold = randomGenerator.nextInt(3) + 1;
                    children.add(new ArrayList<Double>());
                    for (int j = 0; j < population.get(i).size(); j++) {
                        double gene = (j < threshold) ? population.get(p1).get(j) : population.get(p2).get(j);
                        children.get(i).add(gene);
                    }

                    if (randomGenerator.nextDouble() < mutation) {
                        int mutationIdx = randomGenerator.nextInt(children.get(i).size());
                        double gene = children.get(i).get(mutationIdx) + ((randomGenerator.nextDouble() > 0.5) ? 1 : 0);
                        children.get(i).set(mutationIdx, gene);
                    }

                }
                population = children;
            }

            double min = Double.POSITIVE_INFINITY;
            ArrayList<Double> individual = new ArrayList<>();
            for (ArrayList<Double> unit : population) {
                double tmp2 = countY(unit, a, b, c, d);
                if (tmp2 - y < min) {
                    min = tmp2;
                    individual = unit;
                }
            }
            currIteration = it;
            return formatResult(individual);
        }

        String formatResult(ArrayList<Double> x) {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < x.size(); i++) {
                result.append("x" + (i + 1) + " = " + ((double) Math.round(x.get(i) * 100) / 100) + "\n");
            }
            if (prevIteration > currIteration) {
                prevIteration = currIteration;
                result.append("Мутація:" + Double.toString(mutation) + "\nІтерацій: " + Integer.toString(prevIteration) + "\n");
            }
            return result.toString();
        }

        double countY(ArrayList<Double> x, double a, double b,  double c,  double d) {
            return a * x.get(0) + b * x.get(1) + c * x.get(2) + d * x.get(3);
        }
    }
}
