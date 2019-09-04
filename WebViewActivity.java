package com.faisal.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class WebViewActivity extends AppCompatActivity implements OnManageActionListener, ServiceConnection {

        private String Action;
        private String actionUrl;

        FrameLayout layoutOne;
        RelativeLayout layoutTwo;
        ProgressBar webViewProgress;
        View retryLayout;

        TextView internetFailureText;
        private boolean mBounded;
        public LoadService loadService;
        boolean isLockShown = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView( R.layout.activity_webview);


            layoutOne = findViewById(R.id.layoutOne);
            layoutTwo = findViewById(R.id.layoutTwo);
            webViewProgress = findViewById(R.id.webViewProgress);
            retryLayout = findViewById( R.id.failureView);
            Bundle bundle = getIntent().getExtras();
            Action = bundle.getString(LoadService.ACTION);
            actionUrl = bundle.getString(LoadService.ACTION_URL);
            if (LoadService.isServiceRunning(LoadService.class, getApplicationContext())) {
                loadView();
            }else {
                startService(this);
            }
        }

        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();

        }

        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            isLockShown = false;
        }

        private void loadView()
        {
            if (LoadService.serviceManager!=null ) {
                InternalContext.getInstance().setBaseContext(WebViewActivity.this);
                LoadService.serviceManager.addEventListener(this, this);
                if (Action.equalsIgnoreCase(LoadService.ACTION_LOAD)) {
                    if (LoadService.serviceManager.isShownLoadFailure){
                        initRetryView();
                    }else {
                        showView();
                    }
                }
            }
        }
        public void showView(){
            LoadService.serviceManager.mWebview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            if(LoadService.serviceManager.mWebview.getParent() != null) {
                ((ViewGroup)LoadService.serviceManager.mWebview.getParent()).removeView(LoadService.serviceManager.mWebview); // <- fix crash while load second time.
            }
            layoutTwo.addView(LoadService.serviceManager.mWebview);
            if (LoadService.serviceManager.isFinished){
                webViewProgress.setVisibility(View.VISIBLE);
            }
        }

        public void initRetryView(){
            retryLayout.setVisibility(View.VISIBLE);
        }
        @Override
        public void showLoader(boolean isShow, String action) {
            if (Action.equalsIgnoreCase(action)) {
                if (isShow) {
                    webViewProgress.setVisibility(View.VISIBLE);
                } else {
                    webViewProgress.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void quickBack(String action) {
            onBackPressed();
        }
        @Override
        public void onShowRetryView(String action) {
            if (action.equalsIgnoreCase(LoadService.ACTION_LOAD)){
                initRetryView();
            }else {
                initRetryView();
            }
        }
        @Override
        public void onBackPressed() {
            if (Action.equalsIgnoreCase(LoadService.ACTION_LOAD)){
                if (LoadService.serviceManager.canGoBack()) {
                    LoadService.serviceManager.goBack();
                }else {
                    super.onBackPressed();
                }
            }else {
                super.onBackPressed();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        }

        private synchronized void startService(Context context){
            try {
                if (LoadService.isServiceRunning(LoadService.class, getApplicationContext())) {
                    Intent service = new Intent(context, LoadService.class);
                    //context.startService(service);
                    bindService(service, this, BIND_AUTO_CREATE);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoadService.LoadWebviewBinder binder = (LoadService.LoadWebviewBinder) service;
            mBounded = true;
            this.loadService = binder.getService();
            if(this.loadService != null)
            {
                loadView();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            this.loadService = null;
        }
}
