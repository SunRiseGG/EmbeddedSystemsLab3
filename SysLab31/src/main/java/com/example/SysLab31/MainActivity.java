package com.example.SysLab31;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputN;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Button funcBtn = findViewById(R.id.count_btn);
      funcBtn.setOnClickListener(this);

      resultText = findViewById(R.id.result);
      inputN = findViewById(R.id.N_in);
    }

    public void onClick(View v) {
      if(v.getId() == R.id.count_btn) {
        if (inputN.getText().toString().length() > 0) {
          BigInteger n = new BigInteger(inputN.getText().toString());
          Ferma instanceFerma = new Ferma();
          BigInteger[] result = instanceFerma.factorization(n);
          if (result[0] == null) {
            Toast.makeText(this, "Runtime Error", Toast.LENGTH_LONG).show();
          }
          String resultOutput = result[0] + ", " + result[1];
          resultText.setText(resultOutput);
        }
      }
    }

    private static class Ferma {
      public BigInteger[] factorization(BigInteger n) {
        double startTime = System.currentTimeMillis(), timeNow;
        boolean endFlag = false;
        long x = (long) Math.sqrt(n.doubleValue());
        BigInteger[] result = new BigInteger[2];
        while (!endFlag) {
          double z = x * x - n.longValue();
          long y = (long) Math.sqrt(z);
          endFlag = (y == Math.sqrt(z));
          if (endFlag) {
            result[0] = BigInteger.valueOf(Math.abs(x - y));
            result[1] = BigInteger.valueOf(x + y);
          }
          x++;
          if (x > n.longValue()) {
            result[0] = BigInteger.valueOf(1);
            result[1] = n;
            endFlag = true;
          }
          timeNow = System.currentTimeMillis() - startTime;
          if (timeNow > 3000) {
            endFlag = true;
            return result;
          }
        }
        return result;
      }
    }
}
