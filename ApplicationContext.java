package com.faisal.myapplication;

import android.app.Application;
/**
 * Will be called once when the TextSecure process is created.
 *
 * We're using this as an insertion point to patch up the Android PRNG disaster,
 * to initialize the job manager, and to check for GCM registration freshness.
 *
 * @author Moxie Marlinspike
 */
public class ApplicationContext extends Application{

    private static final String TAG = ApplicationContext.class.getSimpleName();
    private static ApplicationContext mContext;
    public static ApplicationContext getContext() {
        return mContext;
    }
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}