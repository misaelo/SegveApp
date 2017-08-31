package com.socomtec.app.tesis.segve.app.segveapp.Servicios;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Misael on 05-jul-17.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "NOTICIAS";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

       String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: " + token);

        enviarTokenAlServidor(token);
    }
    private void enviarTokenAlServidor(String token) {

    }
}