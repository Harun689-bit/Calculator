package com.example.calculator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals, buttonSquare, buttonSqrt, buttonPower, buttonMod;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    private LinkedList<String> history = new LinkedList<>();
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private ListView historyListView;
    private Button btnShowHistory;
    private View bottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        btnShowHistory = findViewById(R.id.btnShowHistory);
        historyListView = findViewById(R.id.historyListView);
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(GONE);

        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        assignId(buttonSquare, R.id.button_square);
        assignId(buttonSqrt, R.id.button_sqrt);
        assignId(buttonPower, R.id.button_power);
        assignId(buttonMod, R.id.button_mod);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);

        btnShowHistory.setOnClickListener(v -> {
            if (bottomSheet.getVisibility() != VISIBLE) {
                updateHistoryList();
                bottomSheet.setVisibility(VISIBLE);
                btnShowHistory.setText("Hide History");
            } else {
                bottomSheet.setVisibility(GONE);
                btnShowHistory.setText("Show History");
            }
        });

        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHistory = history.get(position);
            solutionTv.setText(selectedHistory);
            resultTv.setText(getResult(selectedHistory));
            bottomSheet.setVisibility(GONE);
            btnShowHistory.setText("Show History");
        });

    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if (buttonText.equals("=")) {
            if (history.size() == 5) {
                history.removeFirst();
            }

            StringBuilder builder = new StringBuilder(dataToCalculate);

            history.addFirst(solutionTv.getText().toString());
            solutionTv.setText(resultTv.getText());
            return;
        }
        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        }else if (buttonText.equals("x²")) {
            dataToCalculate = dataToCalculate + "^2";
        } else if (buttonText.equals("√")) {
            // Only add sqrt if expression is not empty
            if (dataToCalculate.isEmpty()) {
                dataToCalculate = "sqrt(";
            } else {
                dataToCalculate = "sqrt(" + dataToCalculate + ")";
            }
        } else if (buttonText.equals("^")) {
            dataToCalculate = dataToCalculate + "^";
        } else if (buttonText.equals("%")) {
            // exp4j doesn't support % operator directly.
            // You can replace '%' with 'mod' function supported by exp4j or handle separately
            dataToCalculate = dataToCalculate + " mod ";
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }

        solutionTv.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if (!finalResult.equals("Err")) {
            resultTv.setText(finalResult);
        }

    }

    public String getResult(String data) {
        try {
            Expression expression = new ExpressionBuilder(data).build();
            double result = expression.evaluate();
            if (result == (long) result) {
                return String.valueOf((long) result);
            } else {
                return String.format("%.4f",result);
            }
        } catch (Exception e) {
            return "Err";
        }
    }

    public void updateHistoryList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                history);
        historyListView.setAdapter(adapter);
    }
}

