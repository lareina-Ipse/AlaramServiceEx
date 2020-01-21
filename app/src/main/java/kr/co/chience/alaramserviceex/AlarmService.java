package kr.co.chience.alaramserviceex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    private boolean isRunning;
    private Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //Foreground에서 실행되면 Notification을 보여줘야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher).build();

            startForeground(1, notification);

        }

        String state = intent.getStringExtra("state");

        if (!this.isRunning && state.equals("on")) {
            //알람음 재생 off. 알람은 시작 상태

            vibrator.vibrate(10000);

            this.isRunning = true;
            Log.e("AlarmService", "Alarm Start");
        } else if (this.isRunning && state.equals("off")) {
            //알람음 재생 on. 알람음 중지 상태.
            vibrator.cancel();
            this.isRunning = false;
            Log.e("AlarmService", "Alarm Stop");

        }

        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String chnnelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(chnnelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return chnnelId;
    }
}
