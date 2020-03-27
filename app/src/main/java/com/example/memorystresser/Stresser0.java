package com.example.memorystresser;

import android.content.Intent;

public class Stresser0 extends StresserService {


    @Override
    public String getProperty() {
        return StresserService.ID_00;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//        super.onStartCommand(intent,flags,startId);
//        return START_NOT_STICKY;
//    }
}
