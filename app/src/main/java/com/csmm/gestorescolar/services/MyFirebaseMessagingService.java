package com.csmm.gestorescolar.services;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if(!message.getData().isEmpty()) {
            System.out.println("Nuevo mensaje: " + message.getData());
        }
    }

    @Override
    public void onNewToken(String token) {
        System.out.println("Nuevo token: " + token);
    }
}
