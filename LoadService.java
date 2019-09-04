package com.faisal.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author : muhammed faisal vv  on 26-03-19
 * this is background service in background
 */
public class LoadService extends Service {
    IBinder mBinder;
    private final LoadWebviewBinder binder                = new LoadWebviewBinder();
    final Handler mHandler = new Handler();
    private Thread mUiThread;
    public static ServiceManager serviceManager;
    public static String ACTION = "ACTION";
    public static String ACTION_LOAD = "ACTION_LOAD";
    public static String ACTION_URL = "ACTION_URL";
    public class LoadWebviewBinder extends Binder {
        public LoadService getService() {
            return LoadService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mUiThread = Thread.currentThread();
        // TODO: MAIL manage to the service.
        serviceManager =new ServiceManager(LoadService.this);
        serviceManager.getInstance(LoadService.this);
        ServiceManager.instance = null;
    }
    @Override
    public int onStartCommand(Intent intent1, int flags, int startId) {
        return super.onStartCommand(intent1, flags, startId);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
    public void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }
    public static boolean isServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}