package net.ddns.gloryweb.dailybudget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import java.util.Date;


public class WidgetProvider extends AppWidgetProvider {
    public float allowance;
    public float curBal;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        allowance = sharedPreferences.getFloat("allowance", 0);
        curBal = sharedPreferences.getFloat("curBal", allowance);

        //compare dates
        Date date1 = new Date();
        String date = String.valueOf(date1.getDate());
        String prevDate = sharedPreferences.getString("prevDate", date);
        int cDate = Integer.parseInt(date);
        int pDate = Integer.parseInt(prevDate);

        if (cDate > pDate) {
            float bankBal = sharedPreferences.getFloat("bankBal", 0);
            float curBal = sharedPreferences.getFloat("curBal", allowance);
            float newBal = (bankBal + curBal) + ((cDate - pDate - 1) * allowance);
            editor.putFloat("bankBal", newBal);
            editor.putFloat("curBal", allowance);
            editor.commit();
        } else if (pDate > cDate) {
            float bankBal = sharedPreferences.getFloat("bankBal", 0);
            float curBal = sharedPreferences.getFloat("curBal", allowance);
            float newBal = (bankBal + curBal);
            editor.putFloat("bankBal", newBal);
            editor.putFloat("curBal", allowance);
            editor.commit();
        }

        editor.putString("prevDate", date);
        editor.commit();

        float curBal = sharedPreferences.getFloat("curBal", 0.0f);
        float bankBal = sharedPreferences.getFloat("bankBal", 0.0f);
        float lastTrans = sharedPreferences.getFloat("lastTransaction", 0.0f);
        String newline = System.getProperty("line.separator");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent configIntent = new Intent(context, MainActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews1 = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews1.setTextViewText(R.id.textView, newline + "$" + String.format("%.2f", curBal));
            remoteViews1.setTextViewText(R.id.textView6, "Bank:" + newline + "$" + String.format("%.2f", bankBal));
            remoteViews1.setTextViewText(R.id.lastTransText, "Last:" + newline + "$" + String.format("%.2f", lastTrans));

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews1);


        }
    }

    public void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int count = appWidgetIds.length;
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
        float curBal = spref.getFloat("curBal", 0.0f);
        float bankBal = spref.getFloat("bankBal", 0.0f);
        float lastTrans = spref.getFloat("lastTransaction", 0.0f);
        String newline = System.getProperty("line.separator");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent configIntent = new Intent(context, MainActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews1 = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews1.setTextViewText(R.id.textView, newline + "$" + String.format("%.2f", curBal));
            remoteViews1.setTextViewText(R.id.lastTransText, "Last:" + newline + String.format("%.2f", lastTrans));
            remoteViews1.setTextViewText(R.id.textView6, "Bank:" + newline + "$" + String.format("%.2f", bankBal));


            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews1);


        }

    }
}
