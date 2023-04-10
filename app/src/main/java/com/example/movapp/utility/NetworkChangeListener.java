package com.example.movapp.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkChangeListener extends BroadcastReceiver {
    public MutableLiveData<Boolean> connection = new MutableLiveData<>();
    public LiveData<Boolean> isConnected(){
        return connection;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.isConnectedToInternet(context)){
            connection.setValue(Boolean.FALSE);
        }else
            connection.setValue(Boolean.TRUE);
    }
}
