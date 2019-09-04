package com.faisal.myapplication;

import android.app.Activity;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * @author : muhammed faisal vv  on 26-03-19
 * this is controller
 */
public class ServiceManager {
    public static ServiceManager instance = null;
    static LoadService context;
    Activity activity;
    public static boolean instanceCreated = false;
    public WebView mWebview ;

    public boolean isFinished = false;
    OnManageActionListener actionListener;

    boolean isShownLoadFailure=false;
    public void addEventListener(OnManageActionListener listener, Activity activity) {
        this.actionListener = listener;
        this.activity=activity;
        //initOpenChooser();
    }
    public ServiceManager(LoadService context) {
        this.context = context;
        // onServiceCallEvents = (OnServiceCallEvents) this;
    }

    public ServiceManager getInstance(LoadService context) {
        instance = new ServiceManager(context);
        instanceCreated = true;
        initView();
        return instance;
    }
    private void initView(){
        //mWebview  = new WebView(context);
        mWebview  = new WebView(InternalContext.getInstance().getMutableContext()!=null?InternalContext.getInstance().getMutableContext():context);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebview.setScrollbarFadingEnabled(true);
        mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.equals("//home")) {
                    // mListener.goAppView();
                    if(actionListener!=null) {
                        actionListener.quickBack(LoadService.ACTION_LOAD);
                    }
                }
                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //isFinished = true;
                if (isFinished == true) {
                }
                return;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                isShownLoadFailure=true;
                if(actionListener!=null) {
                    actionListener.onShowRetryView(LoadService.ACTION_LOAD);
                }
                try {
                    mWebview.stopLoading();
                } catch (Exception e) {
                }

                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                }
                mWebview.loadUrl("about:blank");
            }
        });
        /*download the atached files*/
       // mWebview.setDownloadListener(new CustomDownloadListener());
        mWebview .loadUrl(loadWebUrl());
    }
    public void load(){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebview .loadUrl(loadWebUrl());
            }
        });
    }
    public String loadWebUrl() {
        String url = "";
        url = "url here";
        return url;
    }

    public boolean canGoBack() {
        if (mWebview.getUrl().contains("about:blank")){
            return false;
        }
        return mWebview.canGoBack();
    }

    public void goBack() {
        mWebview.goBack();
    }

    public boolean isFinishMailApp() {
        return isFinished;
    }

}