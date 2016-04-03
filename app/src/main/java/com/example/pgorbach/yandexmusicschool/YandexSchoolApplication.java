package com.example.pgorbach.yandexmusicschool;


import android.app.Application;

import com.orhanobut.logger.Logger;

public class YandexSchoolApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("YandexSchool");
    }
}
