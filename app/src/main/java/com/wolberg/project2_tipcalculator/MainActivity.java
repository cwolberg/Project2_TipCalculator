package com.wolberg.project2_tipcalculator;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import java.text.NumberFormat;
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.view.View;
import android.view.View.OnKeyListener;


public class MainActivity extends AppCompatActivity implements OnEditorActionListener, OnKeyListener, OnSeekBarChangeListener {

    //defining variables for widgets
    private EditText billAmountEditText;
    private TextView percentTextView;
    private TextView tipTotalTextView;
    private TextView totalTextView;
    private SeekBar percentSeekBar;

    // currency and percent formatters
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();


    //definng instance variables
    private String billAmountString ="";
    private float tipPercent = .15f;


    //define shared preferences
    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to widgets
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        tipTotalTextView = (TextView) findViewById(R.id.tipTotalTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);

        //set the listeners
        billAmountEditText.setOnEditorActionListener(this);
        billAmountEditText.setOnKeyListener(this);
        percentSeekBar.setOnSeekBarChangeListener(this);
        percentSeekBar.setOnKeyListener(this);

        //get SharedPreference object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);



    }


    @Override
    protected void onPause(){
        //save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("billAmountString",billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();
        super.onPause();
    }
    @Override
    protected void onResume(){
        //save the instance variables
        super.onResume();
        //get instance variables
        billAmountString = savedValues.getString("billAmountString","");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        // set tip percent
        int progress = Math.round(tipPercent * 100);
        percentSeekBar.setProgress(progress);

        //calculate and display
        calculateAndDisplay();
    }


    private void calculateAndDisplay(){


        //getting bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if(billAmountString.equals("")){
            billAmount = 0;
        }
        else{
            billAmount = Float.parseFloat(billAmountString);
        }

        // get tip percent
        int progress = percentSeekBar.getProgress();
        tipPercent = (float) progress / 100;

        //calculating tip and total
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        //display other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTotalTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));
    }



    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        calculateAndDisplay();
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        percentTextView.setText(progress + "%");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        if(actionId== EditorInfo.IME_ACTION_DONE ||
                actionId==EditorInfo.IME_ACTION_UNSPECIFIED)
        {calculateAndDisplay();}
        return false;
    }


}

