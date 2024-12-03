package com.example.caculator;

import static java.nio.file.Files.find;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private TextView txtInput, txtResult;
    private String currentInput = "";
    private List<String> expression = new ArrayList<>(); // Lưu biểu thức toán học


    private  ImageButton ibtn_return_home ;
//    Button btn_0;
//    Button btn_equal;
//    TextView txt_input;
//    TextView txt_result;

    //----
    private LinearLayout ll_number;
    private LinearLayout ll_euation;
    private LinearLayout ll_convert;
    boolean  check_equation = false;
    boolean check_convert = false;
    private Button btn_euqation;
    private Button btn_convert;
    //----
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        txtInput = findViewById(R.id.txt_input);
        txtResult = findViewById(R.id.txt_result);
        mapping();
        // func thay doi layout Equation, Convert
        Change_Equation();
        Change_Convert();
        // Gán sự kiện cho các nút
        setNumberClickListeners();
        setOperatorClickListeners();
        setUtilityClickListeners();
    }

    //--- ham anh xa -------
    public  void mapping(){
        ll_number = findViewById(R.id.layout_number);
        ll_euation = findViewById(R.id.layout_equation);
        ll_convert = findViewById(R.id.layout_convert);

        btn_euqation = findViewById(R.id.btn_equation);

        btn_convert = findViewById(R.id.btn_convert);

        ibtn_return_home = findViewById(R.id.btn_return_home);
//        //-------
//
//        btn_0 = (Button)  findViewById(R.id.btn_0);
//        btn_equal = (Button) findViewById(R.id.btn_equal);
//        txt_input = (TextView) findViewById(R.id.txt_input);
//        txt_result = (TextView) findViewById(R.id.txt_result);
    }



    private boolean isResultDisplayed = false; // Biến kiểm tra nếu đang hiển thị kết quả


    private void setNumberClickListeners() {
        int[] numberIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_dot
        };

        int[] parenthesisIds = {
                R.id.btn_open_left, R.id.btn_close_right
        };

        // Lắng nghe số và dấu "."
        View.OnClickListener numberListener = view -> {
            if (isResultDisplayed) {
                isResultDisplayed = false; // Chuyển về trạng thái nhập liệu
                expression.clear(); // Xóa biểu thức cũ
                txtResult.setText("Result");
                txtInput.setText(""); // Xóa hiển thị biểu thức cũ
            }
            Button button = (Button) view;
            currentInput += button.getText().toString();
            txtInput.setText(txtInput.getText().toString() + button.getText().toString());
        };


        // Lắng nghe ngoặc
        View.OnClickListener parenthesisListener = view -> {
            Button button = (Button) view;
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            expression.add(button.getText().toString());
            txtInput.setText(txtInput.getText().toString() + button.getText().toString());
        };

        // Gán sự kiện cho số
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        // Gán sự kiện cho ngoặc
        for (int id : parenthesisIds) {
            findViewById(id).setOnClickListener(parenthesisListener);
        }
    }

    private void setOperatorClickListeners() {
        int[] operatorIds = {
                R.id.btn_plus, R.id.btn_minus,
                R.id.btn_multi, R.id.btn_divide
        };

        View.OnClickListener listener = view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            Button button = (Button) view;
            expression.add(button.getText().toString());
            txtInput.setText(txtInput.getText().toString() + " " + button.getText().toString() + " ");
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setUtilityClickListeners() {
        findViewById(R.id.btn_equal).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                expression.add(currentInput);
                currentInput = "";
            }
            try {
                double result = evaluateExpression(expression);
                txtResult.setText(String.valueOf(result));
                isResultDisplayed = true; // Đánh dấu đã hiển thị kết quả
            } catch (Exception e) {
                txtResult.setText("Error");
            }
        });


        findViewById(R.id.btn_CE).setOnClickListener(view -> {
            currentInput = "";
            expression.clear();
            txtInput.setText("");
            txtResult.setText("Result");
        });

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (isResultDisplayed) {
                // Nếu đang hiển thị kết quả, trở lại trạng thái chỉnh sửa biểu thức cũ
                isResultDisplayed = false;
                //txtResult.setText("Result");
                txtInput.setText(formatExpression(expression)); // Hiển thị lại biểu thức
            } else {
                if (!currentInput.isEmpty()) {
                    // Xóa ký tự cuối cùng trong currentInput
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                } else if (!expression.isEmpty()) {
                    // Nếu currentInput rỗng, xóa phần tử cuối cùng trong biểu thức
                    expression.remove(expression.size() - 1);
                }
                // Cập nhật lại giao diện ngay lập tức
                txtInput.setText(formatExpression(expression) + currentInput);
            }
        });


    }

    private String formatExpression(List<String> expression) {
        return String.join(" ", expression) + " ";
    }

    private double evaluateExpression(List<String> expression) {
        List<String> postfix = convertToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private List<String> convertToPostfix(List<String> expression) {
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : expression) {
            if (isNumeric(token)) {
                postfix.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    private double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(operand1 + operand2);
                        break;
                    case "-":
                        stack.push(operand1 - operand2);
                        break;
                    case "X":
                        stack.push(operand1 * operand2);
                        break;
                    case "/":
                        stack.push(operand1 / operand2);
                        break;
                }
            }
        }
        return stack.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "X":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    //=============================================================
    public void Change_Equation(){
        btn_euqation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_equation == false){
                    // Ẩn Layout 1 và hiện Layout 2
                    ll_number.setVisibility(View.GONE);
                    ll_convert.setVisibility(View.GONE);
                    ll_euation.setVisibility(View.VISIBLE);
                    check_equation = true;
                }
                else{
                    // Ẩn Layout 2 và hiện Layout 1
                    ll_euation.setVisibility(View.GONE);
                    ll_convert.setVisibility(View.GONE);
                    ll_number.setVisibility(View.VISIBLE);
                    check_equation = false;
                }

            }
        });

        ibtn_return_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_equation == false){
                    // Ẩn Layout 1 và hiện Layout 2
                    ll_number.setVisibility(View.GONE);
                    ll_convert.setVisibility(View.GONE);
                    ll_euation.setVisibility(View.VISIBLE);
                    check_equation = true;
                }
                else{
                    // Ẩn Layout 2 và hiện Layout 1
                    ll_euation.setVisibility(View.GONE);
                    ll_convert.setVisibility(View.GONE);
                    ll_number.setVisibility(View.VISIBLE);
                    check_equation = false;
                }
            }
        });



    }
    public void Change_Convert(){
        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_convert == false){
                    ll_convert.setVisibility(View.VISIBLE);
                    ll_euation.setVisibility(View.GONE);
                    ll_number.setVisibility(View.GONE);
                    check_convert = true;
                }
                else {
                    ll_convert.setVisibility(View.GONE);
                    ll_euation.setVisibility(View.GONE);
                    ll_number.setVisibility(View.VISIBLE);
                    check_convert = false;
                }
            }
        });
    }

}