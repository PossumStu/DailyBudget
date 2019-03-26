package net.ddns.gloryweb.dailybudget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
//    File budgetData = new File(context.getFilesDir(), dailybudgetdata);
    public float allowance;// = 29.00f;
    private float curBal;
    private float transactionVal;
    private float lastTransaction;
    private TextView dailyBal;
    private TextView bankBal;
    private TextView dailyAllowance;
    private TextView lastTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Intent intent = new Intent(this, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:


        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }


    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        setContentView(R.layout.activity_main);
        dailyBal = (TextView) findViewById(R.id.dailyBal);
        bankBal = (TextView) findViewById(R.id.bankBal);
        dailyAllowance = (TextView) findViewById(R.id.dailyAllowance);
        lastTrans = (TextView) findViewById(R.id.lastTrans);

        //Get shared prefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        allowance = sharedPreferences.getFloat("allowance", 0);
        curBal = sharedPreferences.getFloat("curBal", allowance);
        lastTransaction = sharedPreferences.getFloat("lastTransaction", 0);

        //compare dates
        Date date1 = new Date();
        String date = String.valueOf(date1.getDate());
        String prevDate = sharedPreferences.getString("prevDate", date);
        int cDate = Integer.parseInt(date);
        int pDate = Integer.parseInt(prevDate);

        if(cDate > pDate){
            float bankBal = sharedPreferences.getFloat("bankBal",0);
            float curBal = sharedPreferences.getFloat("curBal", allowance);
            float newBal = (bankBal + curBal) + ((cDate-pDate-1) * allowance);
            editor.putFloat("bankBal", newBal);
            editor.putFloat("curBal", allowance);
            editor.commit();
        }

        else if (pDate > cDate){
            float bankBal = sharedPreferences.getFloat("bankBal",0);
            float curBal = sharedPreferences.getFloat("curBal", allowance);
            float newBal = (bankBal + curBal);
            editor.putFloat("bankBal", newBal);
            editor.putFloat("curBal", allowance);
            editor.commit();
        }


        editor.putString("prevDate", date);
        editor.commit();
        bankBal.setText("$" + String.format("%.2f", sharedPreferences.getFloat("bankBal",0)));
        dailyBal.setText("$" + String.format("%.2f", sharedPreferences.getFloat("curBal", allowance)));
        dailyAllowance.setText("$" +  String.format("%.2f", sharedPreferences.getFloat("allowance", allowance)));
        lastTrans.setText("$" +  String.format("%.2f", sharedPreferences.getFloat("lastTransaction", 0)));

    }



    public void transact(View view) {
        EditText editText = (EditText) findViewById(R.id.transaction);
        String message = editText.getText().toString();

        //sets Last Transaction text view
        lastTrans = (TextView) findViewById(R.id.lastTrans);

        //get shared prefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        curBal = sharedPreferences.getFloat("curBal", allowance);

        //hides keyboard after button press
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //takes input field and uses it to adjust daily balance

        if (message.equals("")) {
            return;}
        else{
            transactionVal = Float.parseFloat(message);
            editor.putFloat("lastTransaction", transactionVal);
            editor.commit();
            lastTrans.setText("$" +  String.format("%.2f", sharedPreferences.getFloat("lastTransaction", 0)));
            curBal = curBal - transactionVal;
            if (dailyBal != null) {
                dailyBal.setText("$" + String.format("%.2f", curBal));
            }
            editText.getText().clear();
        }



        editor.putFloat("curBal", curBal);
        editor.commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void settings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }
}
