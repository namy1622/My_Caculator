package com.example.caculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView txtInput, txtResult;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = Double.NaN;
    private double secondOperand = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        txtInput = findViewById(R.id.txt_input);
        txtResult = findViewById(R.id.txt_result);

        // Gán sự kiện cho các nút
        setNumberClickListeners();
        setOperatorClickListeners();
        setUtilityClickListeners();
    }

    private void setNumberClickListeners() {
        int[] numberIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_dot
        };

        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            currentInput += button.getText().toString();
            txtInput.setText(currentInput);
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorClickListeners() {
        int[] operatorIds = {
                R.id.btn_plus, R.id.btn_minus,
                R.id.btn_multi, R.id.btn_divide
        };

        View.OnClickListener listener = view -> {
            if (!currentInput.isEmpty()) {
                firstOperand = Double.parseDouble(currentInput);
                Button button = (Button) view;
                operator = button.getText().toString();
                currentInput = "";
                txtInput.setText(operator);
            }
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setUtilityClickListeners() {
        findViewById(R.id.btn_equal).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                secondOperand = Double.parseDouble(currentInput);
                double result = calculateResult(firstOperand, secondOperand, operator);
                txtResult.setText(String.valueOf(result));
                currentInput = String.valueOf(result); // Chuẩn bị cho phép tính tiếp theo
                firstOperand = Double.NaN;
                secondOperand = Double.NaN;
                operator = "";
            }
        });

        findViewById(R.id.btn_CE).setOnClickListener(view -> {
            currentInput = "";
            firstOperand = Double.NaN;
            secondOperand = Double.NaN;
            operator = "";
            txtInput.setText("");
            txtResult.setText("Result");
        });

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                txtInput.setText(currentInput);
            }
        });
    }

    private double calculateResult(double operand1, double operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "X":
                return operand1 * operand2;
            case "/":
                if (operand2 != 0) return operand1 / operand2;
                txtResult.setText("Error");
                return Double.NaN;
            default:
                return 0;
        }
    }
}
