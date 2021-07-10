package ru.geekbrains.listofnotes;

import android.app.Application;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;

public class MainApp extends Application {
    private static final VKTokenExpiredHandler tokenTracker = new VKTokenExpiredHandler() {
        @Override
        public void onTokenExpired() {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        VK.addTokenExpiredHandler(tokenTracker);
    }
}
