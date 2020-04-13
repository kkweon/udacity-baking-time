package com.example.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.example.bakingtime.list.MainActivity;

/** Implementation of App Widget functionality. */
public class IngredientsWidget extends AppWidgetProvider {

    private static final int REQUEST_CODE_OPEN_APP = 1;

    static void updateAppWidget(
            Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_RECIPE,
                        Context.MODE_PRIVATE);
        String recipeName =
                sharedPreferences.getString(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_RECIPE_NAME, null);
        String ingredients =
                sharedPreferences.getString(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_INGREDIENTS, null);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.text_view_widget_recipe_title, recipeName);
        views.setTextViewText(R.id.text_view_widget_ingredients, ingredients);

        Intent intent = new Intent(context, MainActivity.class);
        views.setOnClickPendingIntent(
                R.id.widget_container,
                PendingIntent.getActivity(
                        context, REQUEST_CODE_OPEN_APP, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
