package com.dancing_koala.samba.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dancing_koala.samba.R;
import com.dancing_koala.samba.providers.SambaAppWidgetProvider;

/**
 * Created by corentin on 31/05/17.
 */

public class MusicService extends Service {

    public static final String ACTION_MEDIA_COMPLETED = "com.dancing_koala.samba.music_service.MEDIA_COMPLETED";

    private MediaPlayer mMediaplayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaplayer = MediaPlayer.create(this, R.raw.anthem);
        mMediaplayer.setLooping(false);
        mMediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                broadcastMediaCompleted();
                stopSelf();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaplayer.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaplayer.stop();
        mMediaplayer.release();
    }

    private void broadcastMediaCompleted() {
        Intent intent = new Intent(this, SambaAppWidgetProvider.class);
        intent.setAction(ACTION_MEDIA_COMPLETED);
        sendBroadcast(intent);
    }
}
