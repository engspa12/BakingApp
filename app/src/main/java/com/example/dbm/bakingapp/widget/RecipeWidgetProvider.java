package com.example.dbm.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;

public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        RecipeAppWidgetConfigure.updateWidgets(context);
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        RecipeAppWidgetConfigure.deleteWidgets(context,appWidgetIds);
        super.onDeleted(context, appWidgetIds);
    }

}
