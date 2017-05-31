package com.dancing_koala.samba.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.dancing_koala.samba.R;
import com.dancing_koala.samba.services.MusicService;

/**
 * Created by corentin on 31/05/17.
 */

public class SambaAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_WIDGET_CLICK = "com.dancing_koala.samba.appwidget.CLICK";

    private static boolean activated = false;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_samba);
        ComponentName sambaWidget = new ComponentName(context, getClass());

        remoteViews.setOnClickPendingIntent(R.id.mood_icon, getPendingSelfIntent(context, ACTION_WIDGET_CLICK));
        appWidgetManager.updateAppWidget(sambaWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (!ACTION_WIDGET_CLICK.equals(action) && !MusicService.ACTION_MEDIA_COMPLETED.equals(action)) {
            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_samba);
        ComponentName watchWidget = new ComponentName(context, getClass());

        if (ACTION_WIDGET_CLICK.equals(action)) {
            Intent musicServiceIntent = new Intent(context, MusicService.class);

            if (activated) {
                context.stopService(musicServiceIntent);
            } else {
                context.startService(musicServiceIntent);
            }
        }

        int imageResId = activated ? R.drawable.ic_sentiment_neutral : R.drawable.ic_sentiment_happy;

        remoteViews.setImageViewResource(R.id.mood_icon, imageResId);
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        activated = !activated;
    }

    @Override
    public void onDisabled(Context context) {
        if (activated) {
            context.stopService(new Intent(context, MusicService.class));
            activated = false;
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
