package net.ddns.gloryweb.dailybudget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    public float allowance;
    private float newBankBal;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setAllowance(View view) {
        EditText editText = (EditText) findViewById(R.id.dailyAllowanceSet);
        String message = editText.getText().toString();

        //get shared prefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //hides keyboard after button press
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //takes input field and uses it to set new allowance

        if (message.equals("")) {
            return;}
        else{
            allowance = Float.parseFloat(message);
            editor.putFloat("allowance", allowance);
            editor.putFloat("curBal", allowance);
            editText.getText().clear();
            editor.commit();
        }
    }

    public void setBankBal(View view) {
        EditText editText = (EditText) findViewById(R.id.setBankBal);
        String message = editText.getText().toString();

        //get shared prefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //hides keyboard after button press
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //takes input field and uses it to override bank balance

        if (message.equals("")) {
            return;}
        else{
            newBankBal = Float.parseFloat(message);
            editor.putFloat("bankBal", newBankBal);
            editText.getText().clear();
            editor.commit();
        }
    }
}
