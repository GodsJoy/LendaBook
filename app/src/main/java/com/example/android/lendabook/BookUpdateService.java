package com.example.android.lendabook;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by ayomide on 11/13/18.
 * BookUpdateService for widget
 */
public class BookUpdateService extends IntentService {
    public static final String ACTION_UPDATE_WIDGET = "update_widget";
    public BookUpdateService(){
        super("BookUpdateService");
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, BookUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, BookWidgetProvider.class));
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.);
        BookWidgetProvider.updateBookWidget(this, appWidgetManager, appWidgetIds);
    }
}
