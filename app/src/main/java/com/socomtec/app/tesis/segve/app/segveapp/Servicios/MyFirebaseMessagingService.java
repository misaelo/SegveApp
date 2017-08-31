package com.socomtec.app.tesis.segve.app.segveapp.Servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.socomtec.app.tesis.segve.app.segveapp.Activities.MainActivity;
import com.socomtec.app.tesis.segve.app.segveapp.Activities.MapsActivity;
import com.socomtec.app.tesis.segve.app.segveapp.R;

/**
 * Created by Misael on 05-jul-17.
 *
 * AIzaSyByN4Dl8ThHaMbdwSSoM8XHpLkIPo1R6mc
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";
    String lat,lon;
    public static final String MyPREFERENCES = "service" ;
    public static final String latitud = "lat";
    public static final String longitud = "lon";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        FirebaseMessaging.getInstance().subscribeToTopic("TopicName");
        //Log.e("FIREBASE", remoteMessage.getNotification().getBody());
        //Log.i("PVL", "MESSAGE RECEIVED!!");
        String from = remoteMessage.getFrom();
        /*if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Notificacion:::"+ remoteMessage.getNotification().getBody());
        }*/
        if (remoteMessage.getNotification().getBody() != null) {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

            lat = remoteMessage.getData().get("latitud");
            lon = remoteMessage.getData().get("longitud");
            //remoteMessage.getData().get("message")
            //Log.i("PVL", "RECEIVED CORDEna: " + lat + ", lon= " +lon);

            final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(latitud, lat);
            editor.putString(longitud, lon);
            editor.apply();

        } else {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData());
        }
    }
        //sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body")); }


    private void sendNotification(String messageTitle,String messageBody) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_warning_black_24dp)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , notificationBuilder.build());

    }
}
