package com.example.apple.calculatorservice;

import android.app.Application;


public class App extends Application {

    private static int stateCount = 0;


    private
    static App instance;

    public
    static App getInstance() {

        return instance;

    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }

}
