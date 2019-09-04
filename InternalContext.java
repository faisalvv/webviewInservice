package com.faisal.myapplication;

import android.app.Activity;
import android.content.MutableContextWrapper;
/**
 * @author : muhammed faisal vv  on 19-04-19
 * this is class for contex wrapper, switch the context for webview
 * Bug-12006/1,2,8
 */
public class InternalContext {
    private static InternalContext instance;
    private static MutableContextWrapper mutableContext = null;
    public static InternalContext getInstance() {
        if (instance == null) {
            instance = new InternalContext();
            mutableContext = new MutableContextWrapper(ApplicationContext.getContext());
        }
        return instance;
    }
    public void setBaseContext(Activity context) {
        mutableContext.setBaseContext(context);
    }
    public MutableContextWrapper getMutableContext() {
        return mutableContext;
    }
}
