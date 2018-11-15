package com.devgeek.cataloguemovie.alarmmanager;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.devgeek.cataloguemovie.BuildConfig;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by GunSky7 on 6/25/2018.
 */

public class ReleaseReminderReceiver extends BroadcastReceiver {
    public static final String TAG = ReleaseReminderReceiver.class.getSimpleName();

    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public ArrayList<String> listMovie = new ArrayList<>();

    private final int NOTIF_ID_ONE_TIME = 200;
    private final int NOTIF_ID_REPEATING = 201;

    public Context context;

    public ReleaseReminderReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        getUpcomingMovies();
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_access_alarm_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = NOTIF_ID_REPEATING;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Release schedule set up", Toast.LENGTH_SHORT).show();
    }

    private void getUpcomingMovies() {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);

                    JSONObject reponseObject = new JSONObject(result);

                    JSONArray list = reponseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);

                        MovieItem item = new MovieItem(movie);

                        // Change Release Date to the desired format
                        Date d = Calendar.getInstance().getTime();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        String date = formatter.format(d);

                        int notifid = NOTIF_ID_REPEATING;

                        if (date.equals(item.getReleaseDate())) {
                            showAlarmNotification(context, item.getTitle(), "Hari ini " + item.getTitle() + " release", notifid);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
