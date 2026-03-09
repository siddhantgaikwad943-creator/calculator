package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;

    double firstNumber = 0;
    double secondNumber = 0;
    String operator = "";
    boolean isOperatorClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        setNumberClickListeners();
        setOperatorClickListeners();
        setSpecialButtons();

        Button btnTheme = findViewById(R.id.btnTheme);

        btnTheme.setOnClickListener(v -> {

            int currentMode = AppCompatDelegate.getDefaultNightMode();

            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                btnTheme.setText("🌙");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                btnTheme.setText("☀️");
            }

        });
    }

    private void setNumberClickListeners() {
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9
        };

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;

            if (tvResult.getText().toString().equals("0") || isOperatorClicked) {
                tvResult.setText(btn.getText().toString());
                isOperatorClicked = false;
            } else {
                tvResult.append(btn.getText().toString());
            }
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorClickListeners() {
        int[] operatorIds = {
                R.id.btnAdd, R.id.btnSubtract,
                R.id.btnMultiply, R.id.btnDivide
        };

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;

            if (!tvResult.getText().toString().isEmpty()) {

                firstNumber = Double.parseDouble(tvResult.getText().toString());
                operator = btn.getText().toString();

                tvResult.append(operator);   // 👈 THIS LINE ADDED
                isOperatorClicked = false;   // 👈 changed from true
            }
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setSpecialButtons() {

        findViewById(R.id.btnEqual).setOnClickListener(v -> {

            try {
                String currentText = tvResult.getText().toString();
                String[] parts;

                switch (operator) {

                    case "+":
                        parts = currentText.split("\\+");
                        break;

                    case "-":
                        parts = currentText.split("\\-");
                        break;

                    case "*":
                    case "×":
                        parts = currentText.split("[×*]");
                        operator = "*";
                        break;

                    case "/":
                    case "÷":
                        parts = currentText.split("[÷/]");
                        operator = "/";
                        break;

                    default:
                        return;
                }

                if (parts.length < 2) return;

                firstNumber = Double.parseDouble(parts[0]);
                secondNumber = Double.parseDouble(parts[1]);

                double result = 0;

                switch (operator) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "-":
                        result = firstNumber - secondNumber;
                        break;
                    case "*":
                        result = firstNumber * secondNumber;
                        break;
                    case "/":
                        if (secondNumber == 0) {
                            Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        result = firstNumber / secondNumber;
                        break;
                }

                tvResult.setText(String.valueOf(result));

            } catch (Exception e) {
                tvResult.setText("Error");
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            tvResult.setText("0");
            firstNumber = 0;
            secondNumber = 0;
            operator = "";
        });

        findViewById(R.id.btnPercent).setOnClickListener(v -> {

            try {
                String expression = tvResult.getText().toString();

                if (expression.isEmpty()) return;

                // If expression contains operator
                if (expression.contains("+") || expression.contains("-") ||
                        expression.contains("*") || expression.contains("/")) {

                    char operator = 0;
                    int opIndex = -1;

                    for (int i = 0; i < expression.length(); i++) {
                        char ch = expression.charAt(i);
                        if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                            operator = ch;
                            opIndex = i;
                            break;
                        }
                    }

                    if (opIndex == -1) return;

                    double first = Double.parseDouble(expression.substring(0, opIndex));
                    double second = Double.parseDouble(expression.substring(opIndex + 1));

                    double percentValue;

                    if (operator == '+' || operator == '-') {
                        percentValue = first * (second / 100);
                    } else {
                        percentValue = second / 100;
                    }

                    tvResult.setText(expression.substring(0, opIndex + 1) + percentValue);

                } else {
                    // Simple number case
                    double value = Double.parseDouble(expression);
                    value = value / 100;
                    tvResult.setText(String.valueOf(value));
                }

            } catch (Exception e) {
                tvResult.setText("Error");
            }

        });

        findViewById(R.id.btnBackspace).setOnClickListener(v -> {

            String currentText = tvResult.getText().toString();

            if (!currentText.isEmpty() && !currentText.equals("0")) {

                currentText = currentText.substring(0, currentText.length() - 1);

                if (currentText.isEmpty()) {
                    tvResult.setText("0");
                } else {
                    tvResult.setText(currentText);
                }

            }
        });

        findViewById(R.id.btnDot).setOnClickListener(v -> {

            String currentText = tvResult.getText().toString();

            // Get the part after operator
            String lastNumber = currentText;

            if (currentText.contains("+"))
                lastNumber = currentText.substring(currentText.indexOf("+") + 1);
            else if (currentText.contains("-"))
                lastNumber = currentText.substring(currentText.indexOf("-") + 1);
            else if (currentText.contains("*"))
                lastNumber = currentText.substring(currentText.indexOf("*") + 1);
            else if (currentText.contains("/"))
                lastNumber = currentText.substring(currentText.indexOf("/") + 1);

            if (!lastNumber.contains(".")) {
                tvResult.append(".");
            }

        });
    }
}