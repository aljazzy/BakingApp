package com.jatmiko.juli.bakingapp;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Miko on 28/08/2017.
 */

public class MainApp extends Application{
    private static MainApp instance;
    private static Gson gson;
    private EventBus eventBus;

    public MainApp() {
        instance = this;
    }
    public static MainApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createGson();
        createEventBus();
    }
    private void createEventBus() {
        eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    public Gson getGson() {
        return gson;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
