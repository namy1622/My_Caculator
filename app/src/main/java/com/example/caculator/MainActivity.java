package com.example.caculator;

import static com.example.caculator.R.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn_0;
    Button btn_equal;
    TextView txt_input;
    TextView txt_result;

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
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        // --- anh xa ID ---
        mapping();

        // func thay doi layout Equation, Convert
        Change_Equation();
        Change_Convert();

        // -- test thu
        txt_input.setText("");
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_input.setText( txt_input.getText().toString() + "0");

            }
        });

        btn_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_result.setText(txt_input.getText().toString());
            }
        });
        //--
    }

    //--- ham anh xa -------
    public  void mapping(){
        ll_number = findViewById(R.id.layout_number);
        ll_euation = findViewById(R.id.layout_equation);
        ll_convert = findViewById(id.layout_convert);

        btn_euqation = findViewById(R.id.btn_equation);

        btn_convert = findViewById(R.id.btn_convert);
        //-------

        btn_0 = (Button)  findViewById(R.id.btn_0);
        btn_equal = (Button) findViewById(R.id.btn_equal);
        txt_input = (TextView) findViewById(R.id.txt_input);
        txt_result = (TextView) findViewById(R.id.txt_result);
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