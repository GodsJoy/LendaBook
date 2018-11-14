package com.example.android.lendabook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * BookWidgetProvider for book widget
 */
public class BookWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.book_widget_provider);
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        int no_books_borrowed = pref.getInt(context.getString(R.string.no_books_borrowed), MainActivity.default_no_pref);
        int no_books_owned = pref.getInt(context.getString(R.string.no_books_owned), MainActivity.default_no_pref);
        views.setTextViewText(R.id.books_owned, context.getString(R.string.no_books_owned_widget, no_books_owned));
        views.setTextViewText(R.id.books_borrowed, context.getString(R.string.no_books_borrowed_widget, no_books_borrowed));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BookUpdateService.startActionUpdateWidget(context);
    }

    public static void updateBookWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

