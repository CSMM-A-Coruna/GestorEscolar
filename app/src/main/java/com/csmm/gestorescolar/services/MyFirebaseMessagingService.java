package com.csmm.gestorescolar.services;

import android.content.Context;

import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostFCMTokenResponseHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if(!message.getData().isEmpty()) {
            System.out.println("Nuevo mensaje: " + message.getData());
        }
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(String token) {
        RestClient.getInstance(getApplicationContext()).postNewFCMToken(token, new PostFCMTokenResponseHandler() {
            @Override
            public void requestDidComplete() {

            }

            @Override
            public void requestDidFail(int statusCode) {

            }
        });
    }

}
